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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.adapters.ListAdapter
import com.federicocotogno.minimalnotes.data.NoteViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var myNoteViewModel: NoteViewModel
    lateinit var adapter: ListAdapter
    private var orderedByTitle = false

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

        //sort by Title
        myNoteViewModel.orderDataByTitle.observe(viewLifecycleOwner, Observer {
            //wait for user to make request to change data order
        })

        //Show the options menu in this fragment
        setHasOptionsMenu(true)

        swipeRecyclerView()
    }
    private fun swipeRecyclerView() {

        //Adds a swipe to the Recycler View to remove items more fluently
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //gets the not at the position
                    val note = adapter.notesList[viewHolder.adapterPosition]
                    myNoteViewModel.deleteNote(note)

                    Toast.makeText(
                        context,
                        "Note deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_recyclerView)



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu_more, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteAllNotes()
            R.id.nav_sort -> sortNotes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortNotes() {

        //If is false, it will sort data by Title
        if (!orderedByTitle) {
            orderedByTitle = true
            adapter.setData(myNoteViewModel.orderDataByTitle.value!!)
            Toast.makeText(context, "Sorted by Title", Toast.LENGTH_SHORT).show()
        } else {
            //It will sort data by timeStamp
            orderedByTitle = false
            adapter.setData(myNoteViewModel.getAllData.value!!)
            Toast.makeText(context, "Sorted by last edited", Toast.LENGTH_SHORT).show()
        }


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