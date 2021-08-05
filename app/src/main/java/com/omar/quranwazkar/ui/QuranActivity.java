package com.omar.quranwazkar.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.omar.quranwazkar.R;

public class QuranActivity extends AppCompatActivity {

    PDFView pdfView;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_read);
        pdfView = findViewById(R.id.pdfView);
        page = getIntent().getIntExtra("pageNo",1);
        page= page-1;
   //     pdfView.canScrollHorizontally(PDFView.LAYOUT_DIRECTION_LTR);
      //  pdfView.setPositionOffset(PDFView.P);
      //  pdfView.setPositionOffset();
      //  pdfView.computeScroll();


        pdfView.setRotation(180f);

        pdfView.useBestQuality(true);
        pdfView.fromAsset("quran_rotated.pdf").swipeHorizontal(true).pageSnap(true).pageFling(true).autoSpacing(true).
                pageFitPolicy(FitPolicy.BOTH).
       enableAnnotationRendering(true).enableAntialiasing(true).defaultPage(page)

       .load();




    }
}