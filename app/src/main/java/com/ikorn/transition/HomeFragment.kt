package com.ikorn.transition


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ikorn.transition.databinding.FragmentHomeBinding
import com.ikorn.transition.databinding.ItemCardBinding

class HomeFragment : Fragment() {

    val data = listOf("test 1", "test 2", "test 3", "test 4")

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        val recyclerAdapter = HomeContentAdapter(this::onClick)
        with(binding.contents) {
            setHasFixedSize(true)
            PagerSnapHelper().attachToRecyclerView(this)
            adapter = recyclerAdapter
            viewTreeObserver.addOnGlobalLayoutListener {
                startPostponedEnterTransition()
            }
        }
        recyclerAdapter.data = data
    }

    private fun onClick(title: String, map: Map<String, View>) =
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,
                    bundleOf("title" to title), null,
                    FragmentNavigator.Extras.Builder()
                            .addSharedElements(map.entries.associate { it.value to it.key })
                            .build())
                    .also { Log.d("---->", "click $title") }

}

class HomeContentAdapter(val onClick: (String, Map<String, View>) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ContentVH(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as ContentVH).binding) {
            val text = data?.get(position) ?: ""
            Log.d("--->", "data: $text")
            title.text = text
            card.setOnClickListener {
                onClick(text, mapOf<String, View>("card" to card, "title" to title))
            }
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ContentVH(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        constructor(parent: ViewGroup) : this(DataBindingUtil.inflate<ItemCardBinding>(LayoutInflater.from(parent.context), R.layout.item_card, parent, false))
    }
}