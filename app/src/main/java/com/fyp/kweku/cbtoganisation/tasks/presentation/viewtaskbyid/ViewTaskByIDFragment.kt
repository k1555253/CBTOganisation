package com.fyp.kweku.cbtoganisation.tasks.presentation.viewtaskbyid

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

import com.fyp.kweku.cbtoganisation.R
import com.fyp.kweku.cbtoganisation.tasks.presentation.TaskViewModel
import com.fyp.kweku.cbtoganisation.tasks.presentation.edittasks.EditTaskFragment
import com.fyp.kweku.cbtoganisation.tasks.presentation.presentationmodel.TaskPresentationModel
import org.koin.android.ext.android.get
import timber.log.Timber

class ViewTaskByIDFragment : DialogFragment(), ViewTaskByIDViewClassInterface.ViewTaskByIDViewClassFragmentListener {

    lateinit var viewTaskByIDController: ViewTaskByIDController

    companion object {
        val TAG: String = "ViewTaskByIDFragment"
    }

    var taskID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewTaskByIDController = get()
        val taskIDBundle: Bundle? = arguments
        taskID = taskIDBundle?.getString("taskID")
        viewTaskByIDController.loadTask(taskID!!)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val viewTaskByIDViewClassInterface: ViewTaskByIDViewClassInterface = ViewTaskByIDViewClass(inflater, container)
       val taskObserver = Observer<TaskPresentationModel>{ task -> viewTaskByIDViewClassInterface.populateTextViews(task)}
        getSingleTaskLiveDataAsLiveData().observe(viewLifecycleOwner, taskObserver)
        viewTaskByIDViewClassInterface.setFragmentListener(this)
        viewTaskByIDViewClassInterface.setupToolbar()
        return viewTaskByIDViewClassInterface.getRoot()
    }


    private fun getSingleTaskLiveDataAsLiveData(): LiveData<TaskPresentationModel>{
        @Suppress("UNCHECKED_CAST")
       return viewTaskByIDController.observeTask() as LiveData<TaskPresentationModel>
    }


    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun dismissDialogFragment(){
        this.dismiss()
    }

    /*override fun onOptionsItemSelected(item: MenuItem):Boolean= when(item.itemId) {
        R.id.edit_task ->{
            Timber.i("${taskID==null}")
            taskID?.let { launchDialog(it) }
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }

    }*/

    override fun launchEditTaskFragment(){
        taskID?.let { launchEditTaskDialog(it) }
    }

    private fun launchEditTaskDialog(taskID: String){
        launchDialogFragmentWithArguments(taskID)
    }

    private fun launchDialogFragmentWithArguments(taskID: String){

        val taskIDBundle = Bundle()
        taskIDBundle.putString("taskID", taskID)
        launchDialog(taskIDBundle)
    }


    private fun launchDialog(taskIDBundle: Bundle){
        val dialog = EditTaskFragment()
        dialog.arguments = taskIDBundle
        val fragmentTransaction: FragmentTransaction = parentFragment!!.fragmentManager!!.beginTransaction()
        dialog.show(fragmentTransaction, "EditTaskFragment")
    }

}
