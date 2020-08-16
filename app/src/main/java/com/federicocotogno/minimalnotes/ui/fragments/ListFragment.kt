package com.federicocotogno.minimalnotes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.adapters.ListAdapter
import com.federicocotogno.minimalnotes.data.NoteViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var myNoteViewModel: NoteViewModel
    lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Floating action button
        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_newNoteFragment)
        }
        //Setup recycler view
        adapter = ListAdapter()
        rv_recyclerView.adapter = adapter
        rv_recyclerView.layoutManager = LinearLayoutManager(context)

        //Initialise ViewModel
        myNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        //Get the data at app start-up
        myNoteViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        //Show the options menu in this fragment
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteAllNotes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.apply {
            setTitle("Delete all notes?")
            setMessage("Are you sure you want to delete everything?")
            setPositiveButton("Yes") { _, _ ->

                myNoteViewModel.deleteAllNotes()
                Toast.makeText(context, "Everything has been deleted!", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") { _, _ -> }
        }
        dialogBuilder.create().show()
    }
}