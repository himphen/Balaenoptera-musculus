package com.himphen.myapplication.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.himphen.myapplication.databinding.FragmentCurrencyListBinding
import com.himphen.myapplication.ui.currency.adapter.CurrencyListAdapter
import com.himphen.myapplication.ui.currency.viewmodel.CurrencyListViewModel
import com.himphen.myapplication.ui.main.viewmodel.MainViewModel
import com.himphen.myapplication.util.launchCollect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CurrencyListFragment : Fragment() {

    private var viewBinding: FragmentCurrencyListBinding? = null

    private val viewModel: CurrencyListViewModel by viewModel {
        parametersOf()
    }
    private val activityViewModel: MainViewModel by activityViewModels()

    private val listAdapter by lazy {
        CurrencyListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCurrencyListBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.apply {
            vm = viewModel
            lifecycleOwner = this@CurrencyListFragment
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = listAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        recyclerView.context,
                        (recyclerView.layoutManager as LinearLayoutManager).orientation
                    )
                )
            }

            ivSearch.setOnClickListener {
                viewModel.setSearchEnabled()
            }

            ivClose.setOnClickListener {
                searchView.setQuery("", false)
            }

            ivBack.setOnClickListener {
                viewModel.setSearchDisabled()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.updateSearchKeyword(newText)
                    return true
                }

            })
        }

        onObserve()
    }

    private fun onObserve() {
        viewModel.adapterList.launchCollect(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        activityViewModel.selectedData.launchCollect(viewLifecycleOwner) {
            viewModel.updateDataSource(it)
        }
    }

}