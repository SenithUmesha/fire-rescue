package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FAQs extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FAQ> faqList;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        getWindow().setStatusBarColor(ContextCompat.getColor(FAQs.this, R.color.dark_red));

        Init();
        setData();
        setRecycleView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setRecycleView() {
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setData() {
        faqList = new ArrayList<>();

        faqList.add(new FAQ("You Must Be Accountable For Your Actions", "The fire service is not a place for children and that means you have to take full responsibility for your own actions, you don’t pass the buck. When you make mistakes as a firefighter, you own them, you learn from them and you grow. You also understand that there is a chain of command for a reason and that you must follow all reasonable orders and ensure that the officer who gave them is aware that they have been carried out. Accountability is a huge part of successful teamwork too."));
        faqList.add(new FAQ("Show Up Early", "The fire service is not for the semi-committed. You need to get to work at least 30 minutes before you’re due to go on shift. This gives you time to work out what you will be doing and to get familiar with any equipment that you’re going to be called upon to work with during the day."));
        faqList.add(new FAQ("Take Care Of Your Physical Fitness", "The number one cause of death in fire departments today is not fire but heart disease. To be precise, it’s heart attacks. Now, it’s not possible to prevent every heart attack in the world but many can be prevented. Running up and down stairs is a great way to stay fit but you also need to eat right, meditate and work out to ensure that your body can cope with the enormous strains that you’re going to put on it when things get tough. Your life will be much longer and happier for it."));
        faqList.add(new FAQ("Ditch Your Ego", "What nobody needs is the firefighter who brings swaggering overconfidence to the job. These people are dangerous. Once they start thinking they know it all because they’ve “been there and bought the t-shirt”, they stop thinking."));
        faqList.add(new FAQ("Report Your Stress", "Being a firefighter is not easy. Unless you live the most charmed life in the history of the service, you are going to witness trauma during your career. You will get up close to death and horrific injuries and it’s nothing like the experience of seeing this kind of thing on TV."));
        faqList.add(new FAQ("Never Be Afraid To Ask Questions", "Nobody expects you to be psychic or to know everything. If you don’t know something – ask. You won’t look stupid if you ask, but if you mess up because you didn’t ask, you probably will look stupid. The firefighters around you have experience and training too. They have skills that you don’t and vice-versa."));
        faqList.add(new FAQ("Always Check Your Personal Protective Equipment", "Personal Protective Equipment (PPE) is extremely important to your safety as a firefighter. You need to learn how it all goes together and you need to check it before and after each job and each shift to ensure that it’s all in working condition."));
        faqList.add(new FAQ("You Need To Wear Your PPE", "We know that PPE is not always the most comfortable of things. In fact, when you throw a self-contained breathing apparatus into the mix a firefighter might be running around with up to 50lbs of gear on! But here’s the thing – your PPE can only do the job of protecting you if you are wearing it."));
        faqList.add(new FAQ("You Can’t Rush Acceptance", "We know, you want to fit in, and you want to feel like a “real firefighter” on day one in the station. That’s not how it works. Everyone will want to see how you work and how you fit in before they start to feel like you’re a full member of the team."));
        faqList.add(new FAQ("Take Care Of Your Mental Health", "Running up and down stairs ought to help with the physical fitness, but you need to ensure that you keep your brain in tip top form too. You don’t fight fires with your back, you fight them with your mind."));
    }

    private void Init() {
        recyclerView = findViewById(R.id.faq_recyclerView);
        back = findViewById(R.id.faq_back);
    }
}