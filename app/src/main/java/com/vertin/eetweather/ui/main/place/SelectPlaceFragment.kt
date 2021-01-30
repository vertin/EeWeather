package com.vertin.eetweather.ui.main.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChanges
import com.vertin.eetweather.R
import com.vertin.eetweather.databinding.LoadingLayoutBinding
import com.vertin.eetweather.databinding.PlaceItemBinding
import com.vertin.eetweather.databinding.SelectPlaceFragmentBinding
import com.vertin.eetweather.domain.model.PlacePreview
import com.vertin.eetweather.util.PhenomenonUiInterpreter
import com.vertin.eetweather.util.UiResult
import com.vertin.eetweather.util.ViewAnimation
import com.vertin.eetweather.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SelectPlaceFragment : Fragment(R.layout.select_place_fragment) {

    private val placesViewModel by viewModels<PlaceViewModel>()
    private val binding by viewBinding(SelectPlaceFragmentBinding::bind)


    private lateinit var adapter: PlacesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlacesAdapter {
            placesViewModel.assignPlace(it)
        }

        binding.placesListView.layoutManager = LinearLayoutManager(requireContext())

        binding.placesListView.adapter = adapter

        binding.searchEditText.textChanges()
            .observeOn(Schedulers.computation())
            .throttleLast(100, TimeUnit.MILLISECONDS)
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { it.trim() }
            .subscribe { placesViewModel.findPlace(it) }


        placesViewModel.assignSucceed.observe(viewLifecycleOwner, {
            when (it) {
                is UiResult.Loading -> {
                    //ViewAnimation.fadeIn(binding.progressLayout.loadingView)
                }
                is UiResult.Failed -> {
                    // ViewAnimation.fadeOut(binding.progressLayout.loadingView)

                }
                is UiResult.Success -> {
                    findNavController().popBackStack()
                }
            }
        })

        placesViewModel.filteredPlaces.observe(viewLifecycleOwner, {
            when (it) {
                is UiResult.Loading -> {
                    ViewAnimation.fadeIn(binding.progressLayout.loadingView)
                }
                is UiResult.Failed -> {
                    ViewAnimation.fadeOut(binding.progressLayout.loadingView)
                }
                is UiResult.Success -> {
                    ViewAnimation.fadeOut(binding.progressLayout.loadingView)
                    bindData(it.t)
                }
            }
        })
    }

    private fun bindData(places: List<PlacePreview>) {
        binding.emptyView.visibility = if (places.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        adapter.setData(places)

    }

    private class PlacesAdapter(private val onItemClick: (place: PlacePreview) -> Unit) :
        RecyclerView.Adapter<PlaceHolder>() {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlacePreview>() {

            override fun areItemsTheSame(oldItem: PlacePreview, newItem: PlacePreview): Boolean {
                return oldItem.name == newItem.name && oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude
            }

            override fun areContentsTheSame(oldItem: PlacePreview, newItem: PlacePreview): Boolean {
                return oldItem.name == newItem.name && oldItem.temperature == newItem.temperature
            }

        }

        fun setData(value: List<PlacePreview>) {
            mDiffer.submitList(value)
        }

        private val mDiffer: AsyncListDiffer<PlacePreview> =
            AsyncListDiffer(this, DIFF_CALLBACK)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
            val viewBinding = PlaceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val placeHolder = PlaceHolder(viewBinding)
            viewBinding.root.setOnClickListener {
                if (placeHolder.adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(mDiffer.currentList[placeHolder.adapterPosition])
                }
            }
            return placeHolder
        }

        override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
            holder.bind(mDiffer.currentList[position])
        }

        override fun getItemCount(): Int = mDiffer.currentList.size
    }


    class PlaceHolder(private val binding: PlaceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlacePreview) {
            binding.placeNameTextView.text = place.name
            binding.temperature.text = binding.root.context.getString(R.string.temperature, place.temperature)
            val fontName = place.phenomenon?.let {
                val name = PhenomenonUiInterpreter.getPhenomenonAwesomeTest(it)
                binding.weatherType.visibility = View.VISIBLE
                name
            }
            fontName?.let {
                binding.weatherType.text = it
                binding.weatherType.visibility = View.VISIBLE

            } ?: run { binding.weatherType.visibility = View.GONE }
        }
    }
}