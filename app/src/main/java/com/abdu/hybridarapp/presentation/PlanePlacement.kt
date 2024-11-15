package com.abdu.hybridarapp.presentation

import android.os.Bundle
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abdu.hybridarapp.R
import com.abdu.hybridarapp.databinding.FragmentPlanePlacementBinding
import com.abdu.hybridarapp.model.CubeData
import com.abdu.hybridarapp.viewmodel.ARViewState
import com.abdu.hybridarapp.viewmodel.PlacementViewModel
import com.google.ar.core.Config
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.position
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.material.setColor
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.Node
import io.github.sceneview.utils.worldToScreen
import kotlinx.coroutines.launch
import kotlin.math.atan2

class PlanePlacement : Fragment(R.layout.fragment_plane_placement) {

    private var _binding: FragmentPlanePlacementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacementViewModel by viewModels()
    private val cubeNodes = mutableMapOf<String, CubeNode>()
    private lateinit var materialLoader: MaterialLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlanePlacementBinding.bind(view)
        setupSceneView()
        setupUI()
        observeState()
    }

    private fun setupSceneView() {
        materialLoader = MaterialLoader(binding.sceneView.engine, requireContext())
        binding.sceneView.apply {
            configureSession { session, config ->
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.focusMode = Config.FocusMode.AUTO
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
            onFrame = {
                updateUI(viewModel.state.value)
            }
            planeRenderer.isVisible = true

            onTouchEvent = { event, hitResult ->
                if (hitResult == null) {
                    binding.sceneView.frame?.hitTest(event.x, event.y)?.firstOrNull()?.let {
                        val anchor = AnchorNode(binding.sceneView.engine, it.createAnchor())
                        viewModel.placeCube(anchor.worldPosition)
                    }
                }
                false
            }
        }
    }

    private fun setupUI() {
        binding.addCubeButton.setOnClickListener {
            val planes = binding.sceneView.frame?.getUpdatedPlanes()
            planes?.firstOrNull()?.let {
                viewModel.placeCube(it.centerPose.position)
            }

        }

        binding.colorCircle1.setOnClickListener {
            viewModel.changeCubeColor(Color.Red)
        }

        binding.colorCircle2.setOnClickListener {
            viewModel.changeCubeColor(Color.Green)
        }

        binding.colorCircle3.setOnClickListener {
            viewModel.changeCubeColor(Color.Blue)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUI(state)
                updateCubes(state.cubes)
            }
        }
    }

    private fun updateUI(state: ARViewState) {
        if (state.selectedCube != null) {
            binding.cubeInfoCard.visibility = View.VISIBLE
            binding.cubeNameText.text = state.selectedCube.name
            binding.cubePositionText.text = "X: %.2f, Y: %.2f, Z: %.2f".format(
                state.selectedCube.position.x,
                state.selectedCube.position.y,
                state.selectedCube.position.z
            )
            rotateArrowToNode(
                parentView = binding.cubeInfoCard,
                arrowView = binding.directionArrow,
                node = cubeNodes[state.selectedCube.id],
                cameraNode = binding.sceneView.cameraNode
            )
        } else {
            binding.cubeInfoCard.visibility = View.GONE
        }

        binding.sceneView.cameraNode.position = state.cameraOrigin
    }

    private fun updateCubes(cubes: List<CubeData>) {
        // Remove any cubes that are no longer in the list
        val cubesToRemove = cubeNodes.keys.filter { cubeId ->
            !cubes.any { it.id == cubeId }
        }
        cubesToRemove.forEach { cubeId ->
            cubeNodes[cubeId]?.let { node ->
                binding.sceneView.removeChildNode(node)
                cubeNodes.remove(cubeId)
            }
        }

        // Update or create new cubes
        cubes.forEach { cubeData ->
            val existingNode = cubeNodes[cubeData.name]
            if (existingNode != null) {
                existingNode.position = cubeData.position
                updateCubeColor(existingNode, cubeData.color)
            } else {
                // Create new cube
                val cubeNode = CubeNode(
                    engine = binding.sceneView.engine,
                    size = Float3(0.2f, 0.2f, 0.2f)
                ).apply {
                    position = cubeData.position
                    onSingleTapUp = {
                        viewModel.selectCube(cubeData.name)
                        true
                    }
                    materialInstance = materialLoader.createColorInstance(color = cubeData.color)
                }
                updateCubeColor(cubeNode, cubeData.color)
                binding.sceneView.addChildNode(cubeNode)
                cubeNodes[cubeData.name] = cubeNode
            }
        }
    }

    private fun updateCubeColor(cubeNode: CubeNode, color: Color) {
        cubeNode.materialInstance.setColor(color)
    }

    private fun rotateArrowToNode(
        parentView: View,
        arrowView: View,
        node: Node?,
        cameraNode: CameraNode
    ) {
        node?.worldPosition?.let { nodePosition ->
            cameraNode.view?.worldToScreen(nodePosition)?.let { nodeScreenPos ->
                val arrowCenterX = parentView.x
                val arrowCenterY = parentView.y + parentView.height / 2

                val deltaX = nodeScreenPos.x - arrowCenterX
                val deltaY = nodeScreenPos.y - arrowCenterY

                val angle = atan2(deltaY.toDouble(), deltaX.toDouble())

                arrowView.rotation = Math.toDegrees(angle).toFloat() //-90f
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}