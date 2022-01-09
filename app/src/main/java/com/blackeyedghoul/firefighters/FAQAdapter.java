package com.blackeyedghoul.firefighters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQVH> {

    List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQAdapter.FAQVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_card, parent, false);
        return new FAQVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQAdapter.FAQVH holder, int position) {

        FAQ faq = faqList.get(position);
        holder.rowQuestion.setText(faq.getQuestion());
        holder.rowAnswer.setText(faq.getAnswer());

        boolean isExpandable = faqList.get(position).isExpandable();
        holder.relativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public class FAQVH extends RecyclerView.ViewHolder {

        TextView rowQuestion;
        TextView rowAnswer;
        ConstraintLayout constraintLayout;
        RelativeLayout relativeLayout;
        ImageView icon;

        public FAQVH(@NonNull View itemView) {
            super(itemView);

            rowQuestion = itemView.findViewById(R.id.faq_topic);
            rowAnswer = itemView.findViewById(R.id.faq_subtopic);

            constraintLayout = itemView.findViewById(R.id.faq_constraint);
            relativeLayout = itemView.findViewById(R.id.faq_relative);
            icon = itemView.findViewById(R.id.faq_icon);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FAQ faq = faqList.get(getAdapterPosition());
                    faq.setExpandable(!faq.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
