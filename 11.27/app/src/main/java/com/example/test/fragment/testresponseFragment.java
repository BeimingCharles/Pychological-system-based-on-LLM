//package com.example.test.fragment;
//
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.example.test.R;
//
//import java.io.InputStream;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link testresponseFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class testresponseFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    static int ii=0;
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public testresponseFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment testresponseFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static testresponseFragment newInstance(String param1, String param2) {
//        testresponseFragment fragment = new testresponseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        TextView hhhh;
//        hhhh =getView().findViewById(R.id.first);
//
//        // Initialize ScrollView
//        ScrollView scrollView1=getView().findViewById(R.id.scrollView1);
//
//        // Set up click listeners for buttons
//        findViewById(R.id.first).setOnClickListener(this);
//        findViewById(R.id.second).setOnClickListener(this);
//
//
//        // Get the LinearLayout container where the questions will be added
//       // LinearLayout container =getView().findViewById(R.id.questions);
//        InputStream inputStream = null;//=getResources().openRawResource(R.raw.ninerg);
//
//        // Dynamically generate 30 rows
//
//        for (int i = 0; i <ii; i++) {
//            // Create RelativeLayout to hold each row's content
//            RelativeLayout rowLayout = new RelativeLayout(this);
//            LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            rowLayoutParams.setMargins(0, 30, 0, 20); // Set bottom margin for spacing
//            rowLayout.setLayoutParams(rowLayoutParams);
//
//            // Create TextView to display the question
//            TextView textView = new TextView(this);
//            textView.setId(View.generateViewId());  // Unique ID for each TextView
//            textView.setText(fi[i]+ TextChoiceInterfaceControl.ch);  // Display dynamic question text
//            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT, 45);
//            textView.setLayoutParams(textParams);
//            textView.setGravity(Gravity.CENTER);
//
//            // Add the TextView to the row layout
//            rowLayout.addView(textView);
//
//            // Create RadioGroup to hold 7 RadioButtons
//            RadioGroup radioGroup = new RadioGroup(this);
//            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
//            RelativeLayout.LayoutParams groupParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT, 200);
//            groupParams.addRule(RelativeLayout.BELOW, textView.getId());
//            radioGroup.setLayoutParams(groupParams);
//
//            int inc = 1; //
//            int a = 10;
//            int click_count=4;
//
//            // Dynamically generate 7 RadioButtons
//            for (int j = 1 ; j <= click_count; j++) {
//                RadioButton radioButton = new RadioButton(this);
//
//                // Set different width and height to simulate different sizes
//                int width = 150 +inc; // Decreasing width for each button
//                int height = 150 + inc; // Decreasing height for each button
//                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, height);
//
//                // Set margins and gravity
//                params.setMargins((1000-click_count*(150-(10+5*click_count)/2))/(click_count+1), 10, 4, 10);  // Left margin 4dp
//                radioButton.setLayoutParams(params);
//                radioButton.setGravity(Gravity.CENTER);  // Centered
//
//                // Set background resource based on index
//                if (click_count%2==0) {
//                    if(j<=click_count/2){
//                        radioButton.setBackgroundResource(R.drawable.cleckright);
//                        inc-=a;
//                    }
//                    else{
//                        radioButton.setBackgroundResource(R.drawable.cleckk);
//                        inc+=a;
//                    }
//                    // Middle button style
//
//                } else if (click_count%2==1) {
//                    if(j==click_count/2+1){
//                        inc +=a;
//                        radioButton.setBackgroundResource(R.drawable.cleckmid);
//                        inc -= a;
//                    }
//                    else if(j<=click_count/2){
//                        radioButton.setBackgroundResource(R.drawable.cleckright);
//                        inc-=a;
//                    }
//                    else {
//                        radioButton.setBackgroundResource(R.drawable.cleckk);
//                        inc+=a;
//                    }
//
//                }
//
//                // Remove default circular indicator
//                radioButton.setButtonDrawable(null);
//
//                // Add RadioButton to RadioGroup
//                radioGroup.addView(radioButton);
//            }
//
//            // Add RadioGroup to the row layout
//            rowLayout.addView(radioGroup);
//
//            // Add the row layout to the main container
//            container.addView(rowLayout);
//
//            ///////////////////////////////从这开始
//
//            // Set up OnCheckedChangeListener for the RadioGroup
//            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//                // Find the RadioButton that was checked
//                View checkedRadioButton =getView().getViewfindViewById(checkedId);
//                Drawable currentDrawable = checkedRadioButton.getBackground();
//                int Width1 = checkedRadioButton.getWidth();
//                int Height1 = checkedRadioButton.getHeight();
//
//                if (currentDrawable.getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.cleckk).getConstantState())) {
//                    // If the current icon is solid_hook_right, set a different icon
//                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_left);
//                } else if(currentDrawable.getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.cleckmid).getConstantState())){
//                    // If the icon is not solid_hook_right, set it to solid_hook_right
//                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_mid);
//                }else{
//                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_right);
//                }
//
//                for(int k=0;k<radioGroup.getChildCount();k++){
//                    RadioButton radioButton=(RadioButton) radioGroup.getChildAt(k);
//
//                    if(radioGroup.getVisibility()!=View.GONE && radioButton.getId()==checkedId){
//                        int selected=checkedId;
//                        count++;
//                        selected = selected - ((click_count + 1) * (tata- 1) + 1);
//                        toal+=selected;
//
//                        tata++;
//
//                    }
//                }
//
//                // Get the parent row layout
//                RelativeLayout parentRowLayout = (RelativeLayout) (checkedRadioButton.getParent().getParent());
//
//
//                //设置透明度0.4
//                for (int ii = 0; ii < parentRowLayout.getChildCount(); ii++) {
//                    View childView = parentRowLayout.getChildAt(ii);
//                    childView.setAlpha(0.4f); // Set transparency to 40%
//                }
//
//                // Scroll to the row
//                scrollView1.post(() -> scrollView1.smoothScrollTo(0, parentRowLayout.getTop()));
//            });
//
//        }
//
//        return inflater.inflate(R.layout.fragment_testresponse, container, false);
//    }
//}