package pe.com.mipredio;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.Tools;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TaskAdapter";

    private List<TaskModel> mTask = new ArrayList<>();
    private OnTaskListener mOnTaskListener;

    public TaskAdapter(List<TaskModel> mTask, OnTaskListener onTaskListener) {
        this.mTask = mTask;
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

        if (holder instanceof ViewHolder) {

            TaskModel p = this.mTask.get(position);
            ViewHolder view = (ViewHolder) holder;
            view.textViewStatus.setText(p.getEstado().toUpperCase());
            view.textViewMedidor.setText(p.getNroMedidor());
            view.textViewUbigeo.setText(p.getUbigeo());
            view.textViewDireccion.setText(p.getDireccion());

            if(!p.getEstado().equals(Consts._STATUS_PENDING)){
                view.textViewHora.setText( p.getHoraRegistro() ); // Hora en la que el técnico ha llenado la actividad
                view.textViewHora.setVisibility(View.VISIBLE);
            }

            /*
            if (p.getEstado().equals(Consts._STATUS_SEND)) {
                view.imageViewStatus.setImageResource(Consts._IMG_SEND);
                view.imageViewStatus.setColorFilter(ContextCompat.getColor(view.itemView.getContext(), Consts._COLOR_SEND));
                view.textViewSituacion.setVisibility(View.VISIBLE);

            }
            */
            if (p.getEstado().equals(Consts._STATUS_PENDING)) {
                view.imageViewStatus.setImageResource(Consts._IMG_PENDING);
                view.imageViewStatus.setColorFilter(ContextCompat.getColor(view.itemView.getContext(), Consts._COLOR_PENDING));
            } else if (p.getEstado().equals(Consts._STATUS_REGISTER)) {
                view.imageViewStatus.setImageResource(Consts._IMG_REGISTER);
                view.imageViewStatus.setColorFilter(ContextCompat.getColor(view.itemView.getContext(), Consts._COLOR_REGISTER));
            }

            Log.e("ENVIADO",p.getSituacion());

            if (p.getSituacion().equals(Consts._STATUS_SEND)) {
                view.textViewSituacion.setVisibility(View.VISIBLE);
                Log.e("ENVIADO","ok");
            }

        }
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
        // Button buttonStatus;
        ImageView imageViewStatus;
        TextView textViewStatus, textViewMedidor, textViewUbigeo, textViewDireccion, textViewHora, textViewSituacion;
        OnTaskListener mOnTaskListener;

        public ViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            imageViewStatus = (ImageView) itemView.findViewById(R.id.imageViewStatus);
            textViewMedidor = (TextView) itemView.findViewById(R.id.medidor);
            textViewUbigeo = (TextView) itemView.findViewById(R.id.ubigeo);
            textViewDireccion = (TextView) itemView.findViewById(R.id.direccion);
            textViewHora = (TextView) itemView.findViewById(R.id.hora) ;
            textViewSituacion = (TextView) itemView.findViewById(R.id.situacion);
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
