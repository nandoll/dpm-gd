package pe.com.mipredio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.utils.Tools;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TaskAdapter";

    private List<TaskModel> mTask = new ArrayList<>();
    private OnTaskListener mOnTaskListener;

    /*
    public TaskAdapter(ArrayList<TaskModel> mNotes, OnTaskListener onTaskListener) {
        this.mTask = mNotes;
        this.mOnTaskListener = onTaskListener;
    }
     */
    public TaskAdapter(List<TaskModel> mNotes, OnTaskListener onTaskListener) {
        this.mTask = mNotes;
        this.mOnTaskListener = onTaskListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
        return new ViewHolder(view, mOnTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public interface OnTaskListener {
        void onTaskClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TextView timestamp, title;
        OnTaskListener mOnTaskListener;

        public ViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);
            //timestamp = itemView.findViewById(R.id.note_timestamp);
            //title = itemView.findViewById(R.id.note_title);
            mOnTaskListener = onTaskListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnTaskListener.onTaskClick(getAdapterPosition());
        }
    }
}
