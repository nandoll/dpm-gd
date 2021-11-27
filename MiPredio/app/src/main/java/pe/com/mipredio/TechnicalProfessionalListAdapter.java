package pe.com.mipredio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.model.TechnicalProfessionalModel;

public class TechnicalProfessionalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TechProfAdapter";

    private List<TechnicalProfessionalModel> mTechProf = new ArrayList<>();
    private OnTechProfListener mOnTechProfListener;

    public TechnicalProfessionalListAdapter(List<TechnicalProfessionalModel> mTechProf, OnTechProfListener onTechProfListener) {
        this.mTechProf = mTechProf;
        this.mOnTechProfListener = onTechProfListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_technical_professional_list, parent, false);
        return new ViewHolder(view, mOnTechProfListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            TechnicalProfessionalModel p = this.mTechProf.get(position);
            ViewHolder view = (ViewHolder) holder;

            view.textViewNames.setText(p.getNombres());
            view.textViewMail.setText(p.getCorreo());
            view.textViewSpecialty.setText(p.getEspecialidad());

            Picasso.get().load(ApiClient.API_URL_IMAGE_PERSON + p.getFoto()).into(view.circularImageView);

        }
    }

    @Override
    public int getItemCount() {
        return mTechProf.size();
    }

    public interface OnTechProfListener {
        void onTechnicalClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNames, textViewMail, textViewSpecialty;
        OnTechProfListener mOnTechProfListener;
        CircularImageView circularImageView;

        public ViewHolder(View itemView, OnTechProfListener onTechProfListener) {
            super(itemView);
            textViewNames = (TextView) itemView.findViewById(R.id.technicalName);
            textViewMail = (TextView) itemView.findViewById(R.id.technicalMail);
            textViewSpecialty = (TextView) itemView.findViewById(R.id.technicalSpecialty);
            circularImageView = (CircularImageView) itemView.findViewById(R.id.technicalImage);
            mOnTechProfListener = onTechProfListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnTechProfListener.onTechnicalClick(getAdapterPosition());
        }
    }
}
