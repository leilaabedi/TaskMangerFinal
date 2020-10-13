package com.maktab.taskmangerfinal.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maktab.taskmangerfinal.R;
import com.maktab.taskmangerfinal.fragment.ChangeTaskFragment;
import com.maktab.taskmangerfinal.model.Task;
import com.maktab.taskmangerfinal.repository.TaskDBRepository;

import java.util.ArrayList;
import java.util.List;


public class AllFragment extends Fragment {

    public static final String TASK_DETAIL_FRAGMENT = "taskDetailFragment";
    public static final String CHANGE_TASK_FRAGMENT = "changeTaskFragment";


    public static final int REQUEST_CODE_TASK_DETAIL_FRAGMENT = 0;
    public static final int REQUEST_CODE_CHANGE_TASK_FRAGMENT = 5;

    private LinearLayout lay1;
    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;
    // private FloatingActionButton mAddTask;
    private ImageView mEmptyImage;
    private TextView mEmptyText;
    private EditText mTxtSearch;

    private Task mTask = new Task();

    public AllFragment() {
        // Required empty public constructor
    }

    public static AllFragment newInstance() {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        findViews(view);
        initViews();
        setListeners();


        return view;
    }

    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TaskDBRepository taskRepository = TaskDBRepository.getInstance(getActivity());
        List<Task> tasks = taskRepository.getTasks();

        updateUI(tasks);

    }

    private void updateUI(List<Task> tasks) {

        if (tasks.size() != 0) {

            //  mEmptyImage.setVisibility(View.GONE);
            // mEmptyText.setVisibility(View.GONE);

            if (mTaskAdapter == null) {
                mTaskAdapter = new TaskAdapter(tasks);
                mRecyclerView.setAdapter(mTaskAdapter);
            } else {
                mTaskAdapter.setTasks(tasks);
                mTaskAdapter.notifyDataSetChanged();
            }
        }
        //else {
        //  mEmptyImage.setVisibility(View.VISIBLE);
        // mEmptyText.setVisibility(View.VISIBLE);
        //}
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mTxtSearch = view.findViewById(R.id.searchitem);
        lay1 = view.findViewById(R.id.serachoption);

    }


    private void setListeners() {


        mTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //   lay1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchSelect(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchSelect(String str) {
        TaskDBRepository taskRepository = TaskDBRepository.getInstance(getActivity());
        List<Task> tasks = taskRepository.getSearchTask(str);
        updateUI(tasks);
        updateEditUI();


    }


    private class TaskHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle, mTextViewDate, mTextViewIcon;
        private Task mTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.task_item_title);
            mTextViewDate = itemView.findViewById(R.id.task_item_date);
            mTextViewIcon = itemView.findViewById(R.id.task_item_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ChangeTaskFragment changeTaskFragment = ChangeTaskFragment.newInstance(mTask);

                    changeTaskFragment.setTargetFragment(
                            AllFragment.this, REQUEST_CODE_CHANGE_TASK_FRAGMENT);

                    changeTaskFragment.show(getFragmentManager(), CHANGE_TASK_FRAGMENT);

                }
            });
        }

        public void bindTask(Task task) {

            mTask = task;
            mTextViewTitle.setText(task.getTaskTitle());
            mTextViewDate.setText(task.getJustDate() + " " + task.getJustTime());
            if (task.getTaskTitle().length() != 0)
                mTextViewIcon.setText(task.getTaskTitle().charAt(0) + "");

        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.task_row_list, parent, false);

            TaskHolder taskHolder = new TaskHolder(view);
            return taskHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);

        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null)
            return;


        if (requestCode == REQUEST_CODE_CHANGE_TASK_FRAGMENT) {
            switch (resultCode) {
                case ChangeTaskFragment.RESULT_CODE_EDIT_TASK:
                    Task task = (Task) data.getSerializableExtra(ChangeTaskFragment.EXTRA_TASK_CHANGE);
                    TaskDBRepository.getInstance(getActivity()).updateTask(task);
                    updateEditUI();
                    break;
                case ChangeTaskFragment.RESULT_CODE_DELETE_TASK:
                    Task task1 = (Task) data.getSerializableExtra(ChangeTaskFragment.EXTRA_TASK_CHANGE_DELETE);
                    TaskDBRepository.getInstance(getActivity()).removeSingleTask(task1);
                    updateEditUI();
                    break;
                default:
                    break;
            }

        }

    }


    public void updateEditUI() {
        if (mTaskAdapter != null)
            mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateEditUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEditUI();
    }
}