package com.example.rakesh.ui.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rakesh.R
import com.example.rakesh.adapter.gallery.SpaceXAdapter
import com.example.rakesh.databinding.FragmentListBinding
import com.example.rakesh.model.DataResponseItem
import com.example.rakesh.ui.listing.viewModel.ListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingFragment : Fragment(R.layout.fragment_list),
    SpaceXAdapter.OnItemClickLisener {

    private val viewModel by viewModels<ListingViewModel>()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SpaceXAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater).apply { viewModel }
        setupLayout()
        setObserver()
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupLayout() {
        adapter = SpaceXAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }
    }

    private fun setObserver() {
        viewModel.list.observe(viewLifecycleOwner) {
            it?.let { list ->
                adapter.addItems(list)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { message ->
                if (message.isNotEmpty()) {
                    binding.apply {
                        buttonRetry.visibility = View.VISIBLE
                        textViewError.text = message
                        textViewError.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.apply {
                buttonRetry.visibility = View.GONE
                textViewEmpty.visibility = View.GONE
                textViewError.visibility = View.GONE
            }
            it?.let { isLoading ->
                binding.apply {
                    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
                    if (!isLoading && viewModel.list.value.isNullOrEmpty()) {
                        buttonRetry.visibility = View.VISIBLE
                        textViewEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(text: String, data: DataResponseItem, isViewGroup: Boolean) {
        if (isViewGroup) {
            val bundle = Bundle()
            bundle.putParcelable("data", data)
            val action = ListingFragmentDirections.actionGalleryFragmentToDetailFragment(bundle)
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
        }
    }
}