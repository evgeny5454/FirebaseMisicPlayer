package com.evgeny5454.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evgeny5454.musicplayer.adapters.SongAdapter
import com.evgeny5454.musicplayer.databinding.FragmentHomeBinding
import com.evgeny5454.musicplayer.other.Status
import com.evgeny5454.musicplayer.ui.view_models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: FragmentHomeBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        progressBar = binding.allSongsProgressBar
        recyclerView = binding.rvAllSongs
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        subscribeToObservers()

        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() = recyclerView.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    progressBar.isVisible = false

                    result.data?.let { songs ->
                        songAdapter.songs = songs
                    }

                }
                Status.ERROR -> Unit
                Status.LOADING -> progressBar.isVisible = true
            }
        }
    }
}