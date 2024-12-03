package com.himphen.myapplication.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.himphen.myapplication.databinding.ActivityMainBinding
import com.himphen.myapplication.ui.currency.CurrencyListFragment
import com.himphen.myapplication.ui.main.viewmodel.MainViewModel
import com.himphen.myapplication.ui.main.viewmodel.MainViewModelResult
import com.himphen.myapplication.util.launchCollect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val fragment = CurrencyListFragment()

        viewBinding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
            buttonEmpty.setOnClickListener {
                viewModel.emptyDB()
            }

            buttonInsert.setOnClickListener {
                viewModel.insertDB()
            }

            buttonListA.setOnClickListener {
                viewModel.selectDataA()
            }

            buttonListB.setOnClickListener {
                viewModel.selectDataB()
            }

            buttonAll.setOnClickListener {
                viewModel.selectDataAll()
            }
        }

        supportFragmentManager.commit {
            replace(
                viewBinding.fcContainer.id,
                fragment,
                CurrencyListFragment::class.java.simpleName
            )
            addToBackStack(CurrencyListFragment::class.java.simpleName)
        }

        onObserve()
    }

    private fun onObserve() {
        viewModel.vmResult.launchCollect(this) {
            when (it) {
                is MainViewModelResult.EmptyDB.Failed -> {
                    Toast.makeText(this, "Empty DB Failed ${it.message}", Toast.LENGTH_SHORT).show()
                }

                MainViewModelResult.EmptyDB.Success -> {
                    Toast.makeText(this, "Empty DB success", Toast.LENGTH_SHORT).show()
                }

                is MainViewModelResult.InsertDB.Failed -> {
                    Toast.makeText(this, "Insert DB Failed ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

                MainViewModelResult.InsertDB.Success -> {
                    Toast.makeText(this, "Insert DB success", Toast.LENGTH_SHORT).show()
                }

                is MainViewModelResult.SelectData.Failed -> {
                    Toast.makeText(this, "Select Data Failed ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

