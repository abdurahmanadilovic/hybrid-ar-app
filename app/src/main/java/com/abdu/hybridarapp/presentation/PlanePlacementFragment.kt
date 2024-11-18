package com.abdu.hybridarapp.presentation

import android.os.Bundle
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.abdu.hybridarapp.R
import com.abdu.hybridarapp.databinding.FragmentPlanePlacementBinding
import com.abdu.hybridarapp.model.CubeUIModel
import com.google.ar.core.Config
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.material.setColor
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.Node
import io.github.sceneview.utils.worldToScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanePlacementFragment : Fragment(R.layout.fragment_plane_placement) {

    private var _binding: FragmentPlanePlacementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacementViewModel by viewModel<PlacementViewModel>()
    private val cubeNodes = mutableMapOf<String, CubeNode>()
    private lateinit var materialLoader: MaterialLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlanePlacementBinding.bind(view)
        setupUI()
        setupSceneView()
        observeConfigState()
    }

    private fun observeConfigState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.configState.collect { config ->
                when {
                    config.isLoading -> {
                        binding.loadingGroup.visibility = View.VISIBLE
                        binding.contentGroup.visibility = View.INVISIBLE
                        binding.errorGroup.visibility = View.GONE
                    }

                    config.error != null -> {
                        binding.loadingGroup.visibility = View.GONE
                        binding.contentGroup.visibility = View.INVISIBLE
                        binding.errorGroup.visibility = View.VISIBLE
                        binding.errorText.text =
                            config.error.message ?: getString(R.string.an_error_occurred)
                    }

                    config.cameraOrigin != null -> {
                        binding.loadingGroup.visibility = View.GONE
                        binding.errorGroup.visibility = View.GONE
                        binding.contentGroup.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupSceneView() {
        materialLoader = MaterialLoader(binding.sceneView.engine, requireContext())
        binding.sceneView.apply {
            configureSession { session, config ->
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
            onFrame = {
                updateUI(viewModel.state.value)
                updateCubes(viewModel.state.value.cubes)
                updatePlaneCount()
                viewModel.updateTrackedPlanes(frame?.getUpdatedPlanes()?.toList() ?: emptyList())
            }
            planeRenderer.isVisible = true
        }
    }

    private fun setupUI() {
        binding.retryButton.setOnClickListener {
            viewModel.retryInitialization()
        }

        binding.addCubeButton.setOnClickListener {
            viewModel.placeNextCube()
        }

        binding.colorCircle1.setOnClickListener {
            viewModel.changeSelectedCubeColor(Color.Red)
        }

        binding.colorCircle2.setOnClickListener {
            viewModel.changeSelectedCubeColor(Color.Green)
        }

        binding.colorCircle3.setOnClickListener {
            viewModel.changeSelectedCubeColor(Color.Blue)
        }

        binding.cubeInfoCard.setOnClickListener {
            // prevent frame interaction
        }
    }

    private fun updateUI(state: ARViewState) {
        if (state.selectedCube != null) {
            binding.cubeInfoCard.visibility = View.VISIBLE
            binding.cubeNameText.text = state.selectedCube.name
            binding.cubePositionText.text = getString(
                R.string.format_3d_position,
                state.selectedCube.position.x,
                state.selectedCube.position.y,
                state.selectedCube.position.z
            )
            binding.directionArrow.rotation = viewModel.state.value.arrowAngle
            getNextArrowAngle(
                parentView = binding.cubeInfoCard,
                node = cubeNodes[state.selectedCube.id],
                cameraNode = binding.sceneView.cameraNode
            )
        } else {
            binding.cubeInfoCard.visibility = View.GONE
        }
    }

    private fun updateCubes(cubes: List<CubeUIModel>) {
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

        // create new cubes
        cubes.forEach { cubeData ->
            val existingNode = cubeNodes[cubeData.id]
            if (existingNode == null) {
                // Create new cube
                val cubeNode = CubeNode(
                    engine = binding.sceneView.engine,
                    materialInstance = materialLoader.createColorInstance(cubeData.color),
                    size = Float3(0.2f, 0.2f, 0.2f)
                ).apply {
                    position = cubeData.position
                    onSingleTapUp = {
                        viewModel.selectCube(cubeData.id)
                        true
                    }
                }
                binding.sceneView.addChildNode(cubeNode)
                cubeNodes[cubeData.id] = cubeNode
            } else {
                updateCubeColor(existingNode, cubeData.color)
            }
        }
    }

    private fun updateCubeColor(cubeNode: CubeNode, color: Color) {
        cubeNode.materialInstance.setColor(color)
    }

    private fun getNextArrowAngle(
        parentView: View,
        node: Node?,
        cameraNode: CameraNode
    ) {
        node?.worldPosition?.let { nodePosition ->
            cameraNode.view?.worldToScreen(nodePosition)?.let { nodeScreenPos ->
                val arrowCenterX = parentView.x + 50
                val arrowCenterY = parentView.y + parentView.height / 2

                viewModel.updateArrowAngle(
                    arrowCenterX,
                    arrowCenterY,
                    nodeScreenPos.x,
                    nodeScreenPos.y
                )
            }
        }
    }

    private fun updatePlaneCount() {
        binding.planeCountText.text = viewModel.trackedPlanes.size.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}