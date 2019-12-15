package com.example.ttnote.ui.send;


import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.ttnote.R;

public class SendFragment extends Fragment {

    private TextView textView;
    private ImageView imageView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        //init
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        textView = root.findViewById(R.id.name1);
        imageView = root.findViewById(R.id.image);

        String htmlcontent = ("<h1>About Us</h1><ul><li>Nguyễn Thái Ninh Thuận</li><li>Nguyễn Sỹ Tuấn Thành</li><li>Lớp 17T2 - Nhóm 17N11</li></ul>" +"<a href=\"https://github.com/ninhthuanntnt/Android_CND-\">Link gitHud:</a>");
        textView.setText(Html.fromHtml(htmlcontent));
        imageView.setImageResource(R.drawable.borntoshine);
        return root;
    }
}
