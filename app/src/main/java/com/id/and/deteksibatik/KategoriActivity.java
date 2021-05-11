package com.id.and.deteksibatik;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.id.and.deteksibatik.fragment.FragmentA;
import com.id.and.deteksibatik.fragment.FragmentAA;
import com.id.and.deteksibatik.fragment.FragmentB;
import com.id.and.deteksibatik.fragment.FragmentBB;
import com.id.and.deteksibatik.fragment.FragmentC;
import com.id.and.deteksibatik.fragment.FragmentCC;
import com.id.and.deteksibatik.fragment.FragmentD;
import com.id.and.deteksibatik.fragment.FragmentDD;
import com.id.and.deteksibatik.fragment.FragmentE;
import com.id.and.deteksibatik.fragment.FragmentEE;
import com.id.and.deteksibatik.fragment.FragmentF;
import com.id.and.deteksibatik.fragment.FragmentFF;
import com.id.and.deteksibatik.fragment.FragmentG;
import com.id.and.deteksibatik.fragment.FragmentGG;
import com.id.and.deteksibatik.fragment.FragmentH;
import com.id.and.deteksibatik.fragment.FragmentHH;
import com.id.and.deteksibatik.fragment.FragmentI;
import com.id.and.deteksibatik.fragment.FragmentII;
import com.id.and.deteksibatik.fragment.FragmentJ;
import com.id.and.deteksibatik.fragment.FragmentJJ;
import com.id.and.deteksibatik.fragment.FragmentK;
import com.id.and.deteksibatik.fragment.FragmentKK;
import com.id.and.deteksibatik.fragment.FragmentL;
import com.id.and.deteksibatik.fragment.FragmentLL;
import com.id.and.deteksibatik.fragment.FragmentM;
import com.id.and.deteksibatik.fragment.FragmentMM;
import com.id.and.deteksibatik.fragment.FragmentN;
import com.id.and.deteksibatik.fragment.FragmentNN;
import com.id.and.deteksibatik.fragment.FragmentO;
import com.id.and.deteksibatik.fragment.FragmentOO;
import com.id.and.deteksibatik.fragment.FragmentP;
import com.id.and.deteksibatik.fragment.FragmentPP;
import com.id.and.deteksibatik.fragment.FragmentQ;
import com.id.and.deteksibatik.fragment.FragmentQQ;
import com.id.and.deteksibatik.fragment.FragmentR;
import com.id.and.deteksibatik.fragment.FragmentRR;
import com.id.and.deteksibatik.fragment.FragmentS;
import com.id.and.deteksibatik.fragment.FragmentSS;
import com.id.and.deteksibatik.fragment.FragmentT;
import com.id.and.deteksibatik.fragment.FragmentTT;
import com.id.and.deteksibatik.fragment.FragmentU;
import com.id.and.deteksibatik.fragment.FragmentUU;
import com.id.and.deteksibatik.fragment.FragmentV;
import com.id.and.deteksibatik.fragment.FragmentVV;
import com.id.and.deteksibatik.fragment.FragmentW;
import com.id.and.deteksibatik.fragment.FragmentWW;
import com.id.and.deteksibatik.fragment.FragmentX;
import com.id.and.deteksibatik.fragment.FragmentXX;
import com.id.and.deteksibatik.fragment.FragmentY;
import com.id.and.deteksibatik.fragment.FragmentZ;

import java.util.ArrayList;
import java.util.List;

public class KategoriActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentA fragmenta  = new FragmentA();
        FragmentB fragmentb = new FragmentB();
        FragmentC fragmentc  = new FragmentC();
        FragmentD fragmentd = new FragmentD();
        FragmentE fragmente  = new FragmentE();
        FragmentF fragmentf = new FragmentF();
        FragmentG fragmentg  = new FragmentG();
        FragmentH fragmenth = new FragmentH();
        FragmentI fragmenti  = new FragmentI();
        FragmentJ fragmentj = new FragmentJ();
        FragmentK fragmentk  = new FragmentK();
        FragmentL fragmentl = new FragmentL();
        FragmentM fragmentm  = new FragmentM();
        FragmentN fragmentn = new FragmentN();
        FragmentO fragmento  = new FragmentO();
        FragmentP fragmentp = new FragmentP();
        FragmentQ fragmentq  = new FragmentQ();
        FragmentR fragmentr = new FragmentR();
        FragmentS fragments  = new FragmentS();
        FragmentT fragmentt = new FragmentT();
        FragmentU fragmentu  = new FragmentU();
        FragmentV fragmentv = new FragmentV();
        FragmentW fragmentw  = new FragmentW();
        FragmentX fragmentx = new FragmentX();
        FragmentY fragmenty  = new FragmentY();
        FragmentZ fragmentz = new FragmentZ();
        FragmentAA fragmentaa  = new FragmentAA();
        FragmentBB fragmentbb = new FragmentBB();
        FragmentCC fragmentcc  = new FragmentCC();
        FragmentDD fragmentdd = new FragmentDD();
        FragmentEE fragmentee  = new FragmentEE();
        FragmentFF fragmentff = new FragmentFF();
        FragmentGG fragmentgg  = new FragmentGG();
        FragmentHH fragmenthh = new FragmentHH();
        FragmentII fragmentii  = new FragmentII();
        FragmentJJ fragmentjj = new FragmentJJ();
        FragmentKK fragmentkk  = new FragmentKK();
        FragmentLL fragmentll = new FragmentLL();
        FragmentMM fragmentmm  = new FragmentMM();
        FragmentNN fragmentnn = new FragmentNN();
        FragmentOO fragmentoo  = new FragmentOO();
        FragmentPP fragmentpp = new FragmentPP();
        FragmentQQ fragmentqq  = new FragmentQQ();
        FragmentRR fragmentrr = new FragmentRR();
        FragmentSS fragmentss  = new FragmentSS();
        FragmentTT fragmenttt = new FragmentTT();
        FragmentUU fragmentuu  = new FragmentUU();
        FragmentVV fragmentvv = new FragmentVV();
        FragmentWW fragmentww  = new FragmentWW();
        FragmentXX fragmentxx = new FragmentXX();
        adapter.addFragment(fragmenta ,"B1");
        adapter.addFragment(fragmentb ,"B2");
        adapter.addFragment(fragmentc ,"B3");
        adapter.addFragment(fragmentd ,"B4");
        adapter.addFragment(fragmente ,"B5");
        adapter.addFragment(fragmentf ,"B6");
        adapter.addFragment(fragmentg ,"B7");
        adapter.addFragment(fragmenth ,"B8");
        adapter.addFragment(fragmenti ,"B9");
        adapter.addFragment(fragmentj ,"B10");
        adapter.addFragment(fragmentk ,"B11");
        adapter.addFragment(fragmentl ,"B12");
        adapter.addFragment(fragmentm ,"B13");
        adapter.addFragment(fragmentn ,"B14");
        adapter.addFragment(fragmento ,"B15");
        adapter.addFragment(fragmentp ,"B16");
        adapter.addFragment(fragmentq ,"B17");
        adapter.addFragment(fragmentr ,"B18");
        adapter.addFragment(fragments ,"B19");
        adapter.addFragment(fragmentt ,"B20");
        adapter.addFragment(fragmentu ,"B21");
        adapter.addFragment(fragmentv ,"B22");
        adapter.addFragment(fragmentw ,"B23");
        adapter.addFragment(fragmentx ,"B24");
        adapter.addFragment(fragmenty ,"B25");
        adapter.addFragment(fragmentz ,"B26");
        adapter.addFragment(fragmentaa ,"B27");
        adapter.addFragment(fragmentbb ,"B28");
        adapter.addFragment(fragmentcc ,"B29");
        adapter.addFragment(fragmentdd ,"B30");
        adapter.addFragment(fragmentee ,"B31");
        adapter.addFragment(fragmentff ,"B32");
        adapter.addFragment(fragmentgg ,"B33");
        adapter.addFragment(fragmenthh ,"B34");
        adapter.addFragment(fragmentii ,"B35");
        adapter.addFragment(fragmentjj ,"B36");
        adapter.addFragment(fragmentkk ,"B37");
        adapter.addFragment(fragmentll ,"B38");
        adapter.addFragment(fragmentmm ,"B39");
        adapter.addFragment(fragmentnn ,"B40");
        adapter.addFragment(fragmentoo ,"B41");
        adapter.addFragment(fragmentpp ,"B42");
        adapter.addFragment(fragmentqq ,"B43");
        adapter.addFragment(fragmentrr ,"B44");
        adapter.addFragment(fragmentss ,"B45");
        adapter.addFragment(fragmenttt ,"B46");
        adapter.addFragment(fragmentuu ,"B47");
        adapter.addFragment(fragmentvv ,"B48");
        adapter.addFragment(fragmentww ,"B49");
        adapter.addFragment(fragmentxx ,"B50");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {


            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.putExtra("EXIT", true);
        startActivity(home);
        finish();
    }
}
