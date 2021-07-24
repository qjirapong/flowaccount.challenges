package com.android.pouch.ui.transaction.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.pouch.R
import com.android.pouch.databinding.FragmentTransactionListBinding
import com.android.pouch.ui.base.ProgressableFragment
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionListFragment : ProgressableFragment() {

    private val viewModel: TransactionListViewModel by viewModels()
    private lateinit var binding: FragmentTransactionListBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(transaction_list_toolbar)
        (activity as AppCompatActivity).supportActionBar!!.title = "Transaction List"

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showProgressDialog(isLoading)
        }

        viewModel.transactions.observe(viewLifecycleOwner) { transitions ->
            binding.transactionList.adapter = TransactionListAdapter(transitions)
            binding.transactionList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.transactionList.setHasFixedSize(true)
            binding.swipeToRefresh.isRefreshing = false
        }

        binding.createTransactionFab.setOnClickListener {
            findNavController().navigate(R.id.action_TransactionListFragment_to_TransactionDetailFragment)
        }

        binding.swipeToRefresh.setOnRefreshListener { refreshData() }
    }

    private fun refreshData() {
        viewModel.loadTransactions()
    }
}