package com.abdu.hybridarapp.presentation

import android.app.Fragment
import android.os.Bundle
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.abdu.hybridarapp.R
import com.abdu.hybridarapp.databinding.FragmentPlanePlacementBinding
import com.abdu.hybridarapp.viewmodel.PlacementViewModel
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.Point
import dev.romainguy.kotlin.math.Float3
import kotlinx.coroutines.launch
import kotlin.math.atan2

class PlanePlacement : Fragment(){

    private var _binding: FragmentPlanePlacementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacementViewModel by viewModels()
    private val cubeNodes = mutableMapOf<String, CubeNode>()
    lateinit var materialLoader: MaterialLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlanePlacementBinding.bind(view)

        materialLoader = MaterialLoader(binding.sceneView.engine, requireContext())
        setupSceneView()
        setupUI()
        observeState()
    }

    private fun setupSceneView() {
        binding.sceneView.apply {
            configureSession { session, config ->
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.focusMode = Config.FocusMode.AUTO
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
            frame?.camera?.viewTransform
            onFrame = {
                updateUI(viewModel.state.value)
            }
            planeRenderer.isVisible = true

            onTouchEvent = { event, hitResult ->
                if (hitResult == null) {
                    binding.sceneView.frame?.hitTest(event.x, event.y)?.firstOrNull()?.let {

                        val anchor = AnchorNode(binding.sceneView.engine, it.createAnchor())

                        val addCube = AddCubeToView(
                            CreateAndAddCube(
                                GetInitialWorldPosition(NodesRepositoryImpl(ApiServiceImpl()))
                            )
                        )
                        viewLifecycleOwner.lifecycleScope.launch {
                            addCube(
                                tapLocation = Float3Mapper.toPosition3d(anchor.worldPosition),
                                arView = this@PlanePlacement
                            )
                        }
                        viewModel.placeCube(anchor.worldPosition)
                    }
                }
                false
            }
        }
    }

    private fun setupUI() {
        binding.addCubeButton.setOnClickListener {
            val trackables = binding.sceneView.frame?.getUpdatedTrackables()

            println("PlanePlacement.setupUI trackables size: ${trackables?.size}")

            trackables?.let {
                if (it.isNotEmpty()) {
                    val currentTrackable = it.first()
                    if (currentTrackable is Plane) {
                        println("PlanePlacement.setupUI currentTrackable is Plane")
                        viewModel.placeCube(currentTrackable.centerPose.position)
                    } else if (currentTrackable is Point) {
                        println("PlanePlacement.setupUI currentTrackable is not Plane")
//                        currentTrackable.createAnchor(currentTrackable.pose)
                    } else {
                        println("PlanePlacement.setupUI currentTrackable is not Plane or Point")
                    }
                }
            }
            viewModel.startPlacingCube()
        }

        binding.colorCircle1.setOnClickListener {
            val state = viewModel.state.value
            if (state is PlacementState.CubeSelected) {
                viewModel.changeCubeColor(state.cubeData.id, Color.Red)
            }
        }

        binding.colorCircle2.setOnClickListener {
            val state = viewModel.state.value
            if (state is PlacementState.CubeSelected) {
                viewModel.changeCubeColor(state.cubeData.id, Color.Red)
            }
        }

        binding.colorCircle3.setOnClickListener {
            val state = viewModel.state.value
            if (state is PlacementState.CubeSelected) {
                viewModel.changeCubeColor(state.cubeData.id, Color.Red)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUI(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cubes.collect { cubes ->
                updateCubes(cubes)
            }
        }
    }

    private fun updateUI(state: PlacementState) {
        when (state) {
            is PlacementState.Idle -> {
                binding.cubeInfoCard.visibility = View.GONE
                binding.sceneView.planeRenderer.isVisible = false
            }

            is PlacementState.PlacingCube -> {
                binding.cubeInfoCard.visibility = View.GONE
                binding.sceneView.planeRenderer.isVisible = true
            }

            is PlacementState.CubeSelected -> {
                binding.cubeInfoCard.visibility = View.VISIBLE
                binding.cubeNameText.text = state.cubeData.name
                binding.cubePositionText.text = "X: %.2f, Y: %.2f, Z: %.2f".format(
                    state.cubeData.position.x,
                    state.cubeData.position.y,
                    state.cubeData.position.z
                )
                rotateArrowToNode(
                    parentView = binding.cubeInfoCard,
                    arrowView = binding.directionArrow,
                    node = cubeNodes[state.cubeData.id],
                    cameraNode = binding.sceneView.cameraNode
                )
            }
        }
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

        // Update or add cubes
        cubes.forEach { cubeData ->
        }
    }

    private fun updateCubeColor(cubeNode: CubeNode, color: Color) {
        cubeNode.materialInstance.setColor(color)
    }

    fun rotateArrowToNode(parentView: View, arrowView: View, node: Node?, cameraNode: CameraNode) {
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

    override fun addCube(domainCube: DomainCube) {
        val existingNode = cubeNodes[domainCube.name]
        if (existingNode != null) {
            // Update existing cube
            existingNode.position = Float3Mapper.toFloat3(domainCube.position)
            updateCubeColor(existingNode, domainCube.color)
        } else {
            // Create new cube
            val cubeNode = CubeNode(
                engine = binding.sceneView.engine,
                size = Float3(0.2f, 0.2f, 0.2f)
            ).apply {
                position = Float3Mapper.toFloat3(domainCube.position)
                onSingleTapUp = {
                    viewModel.selectCube(domainCube.name)
                    true
                }
                materialInstance = materialLoader.createColorInstance(color = Color.Red)
            }
            updateCubeColor(cubeNode, domainCube.color)
            binding.sceneView.addChildNode(cubeNode)
            cubeNodes[domainCube.name] = cubeNode
        }
    }

    override fun removeCube(id: String) {
        cubeNodes[id]?.let { node ->
            binding.sceneView.removeChildNode(node)
            cubeNodes.remove(id)
        }
    }

    override fun setCameraOrigin(position3d: Position3d) {
        binding.sceneView.cameraNode.position = Float3Mapper.toFloat3(position3d)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}