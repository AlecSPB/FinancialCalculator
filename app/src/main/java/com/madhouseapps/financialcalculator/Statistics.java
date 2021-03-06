package com.madhouseapps.financialcalculator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.madhouseapps.financialcalculator.TanishqTable.TableRow;

import java.util.ArrayList;


public class Statistics extends AppCompatActivity {

    double rate;
    float amount;
    int tenure;
    int tenure_type; /** 1 for Year - 0 for Month **/
    double compounding; /** Only for FD, 1 for Monthly, 2 for Quarterly, 3 for Half Yearly, 4 for Yearly **/
    int calculation; /** 1 for EMI, 2 for FD, 3 for RD, 4 for SIP, 5 for RP **/
    float MV; /** for FD, RD and SIP **/
    float interest;
    float emi;
    float total_return; /** Only for RP **/
    float total_invested; /** Only for RP **/
    float savings_per_month; /** Only for RP **/

    PieChart chart;
    Typeface poppins_bold;

    Description desc;
    LinearLayout tableArea, tableHead;
    LinearLayout linearViewOfTable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        poppins_bold = Typeface.createFromAsset(getAssets(), "fonts/poppinsb.ttf");

        tableArea = (LinearLayout) findViewById(R.id.tableArea);
        tableHead = (LinearLayout) findViewById(R.id.tableHead);
        linearViewOfTable  = (LinearLayout) findViewById(R.id.linearViewOfTable);

        Intent intent = getIntent();
        rate = intent.getDoubleExtra("Rate", 1);
        amount = intent.getFloatExtra("Amount", 1);
        tenure = intent.getIntExtra("Tenure", 1);
        tenure_type = intent.getIntExtra("TenureType", 1);
        compounding = intent.getDoubleExtra("Compounding", 1);
        calculation = intent.getIntExtra("Calculation", 1);
        MV = intent.getFloatExtra("MV", 1);
        interest = intent.getFloatExtra("Interest", 1);
        emi = intent.getFloatExtra("EMI", 1);
        total_return = intent.getFloatExtra("TotalReturn", 0);
        total_invested = intent.getFloatExtra("TotalInvested", 0);
        savings_per_month = intent.getFloatExtra("SavingsPerMonth", 0);


        desc = new Description();
        desc.setTextColor(Color.parseColor("#FFFFFF"));


        chart = (PieChart) findViewById(R.id.chart);

        switch (calculation){
            case 1:
                drawEMIGraph();
                break;
            case 2:
                drawFDGraph();
                break;
            case 3:
                drawRDGraph();
                break;
            case 4:
                drawSIPGraph();
                break;
            case 5:
                drawRPGraph();
                break;
        }

    }

    //emigraph
    public void drawEMIGraph(){

        if(tenure_type==1){
            tableHead.addView(new TableRow(this, "Month", "Principal", "Interest", "Principal Balance"));
        } else {
            tableHead.addView(new TableRow(this, "Month", "Principal", "Interest", "Principal Balance"));
        }


        double loanA = amount;
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(amount, "Loan Amount"));
        yvalues.add(new PieEntry(interest, "Interest"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.primary_emi));
        colors.add(Color.parseColor("#FF5252"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_emi));

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14);
        pieData.setValueTypeface(poppins_bold);

        chart.setData(pieData);
        chart.setCenterText("Total Payable: "+(amount+interest));
        chart.setCenterTextTypeface(poppins_bold);
        chart.animateX(500);
        chart.setCenterTextColor(getResources().getColor(R.color.primary_emi));
        desc.setText("EMI Scheme");
        desc.setTypeface(poppins_bold);
        chart.setDescription(desc);
        chart.getLegend().setTextColor(Color.WHITE);

        if(tenure_type==1){
            tenure = tenure * 12;
        } else {
            //do nothing
        }


        double fullReturn = emi * tenure;
        /*

        for(int i=0; i<(tenure); i++){
            fullReturn = fullReturn-emi;
            tableArea.addView(new TableRow(this, ""+(i+1), ""+emi, ""+Math.round(fullReturn)));
        }
        */

        rate = rate / 100;
        for(int i=0; i<tenure; i++){
            //double P = (rate*(amount)) / (1-(Math.pow((1+rate), -1*tenure)));
            double I = (amount * rate)/12;
            double P = emi - I;
            amount = (float) (amount - P);
            fullReturn = fullReturn-emi;
            tableArea.addView(new TableRow(this, ""+(i+1), ""+Math.round(P), ""+Math.round(I),""+Math.round(amount)));

        }


    }

    //fdgraph
    public void drawFDGraph(){

        /**
        if(tenure_type==1){
            tableHead.addView(new TableRow(this, "Year", "Interest", "Value"));
        } else {
            tableHead.addView(new TableRow(this, "Month", "Interest", "Value"));
        }
         **/

        TextView fdwarning = new TextView(this);
        fdwarning.setText(R.string.fd_stats_warning);
        fdwarning.setTypeface(poppins_bold);
        fdwarning.setTextColor(getResources().getColor(R.color.primary_fd));
        linearViewOfTable.addView(fdwarning);

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(amount, "Amount"));
        yvalues.add(new PieEntry(interest, "Interest"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.primary_fd));
        colors.add(Color.parseColor("#2196F3"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_fd));

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14);
        pieData.setValueTypeface(poppins_bold);

        chart.setData(pieData);
        chart.setCenterText("Maturity Value: "+MV);
        chart.setCenterTextTypeface(poppins_bold);
        chart.animateX(500);
        chart.setCenterTextColor(getResources().getColor(R.color.primary_fd));
        desc.setText("Fixed Deposit Return");
        desc.setTypeface(poppins_bold);
        chart.setDescription(desc);
        chart.getLegend().setTextColor(Color.WHITE);

        /**
        //change between yearly
        if(tenure_type==1){
            rate = rate / 400;
            
        } else {
            rate = rate / 1200;
            double sum_inte=0;
            for(int i=0; i<tenure; i++){
                double inte = (rate) * amount;
                sum_inte = sum_inte + inte;
                amount = (float) (amount + inte);
                tableArea.addView(new TableRow(this, ""+(i+1), ""+Math.round(inte), ""+Math.round(amount)));
            }
        }

         **/

    }

    //rdgraph
    public void drawRDGraph(){
        if(tenure_type==1){
            tableHead.addView(new TableRow(this, "Month", "Interest", "Value"));
        } else {
            tableHead.addView(new TableRow(this, "Month", "Interest", "Value"));
        }

        if(tenure_type==1){
            tenure=tenure*12;
        }

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(amount*tenure, "Amount"));
        yvalues.add(new PieEntry(interest, "Interest"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.primary_rd));
        colors.add(Color.parseColor("#388E3C"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_rd));

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14);
        pieData.setValueTypeface(poppins_bold);

        chart.setData(pieData);
        chart.setCenterText("Return: "+MV);
        chart.setCenterTextTypeface(poppins_bold);
        chart.animateX(500);
        chart.setCenterTextColor(getResources().getColor(R.color.primary_rd));
        desc.setText("Recurring Deposit Return");
        desc.setTypeface(poppins_bold);
        chart.setDescription(desc);
        chart.getLegend().setTextColor(Color.WHITE);


        double rate2 = (rate) / 400;
        double local_tenure = tenure;
        double year_tenure;

        //getting all the inputs perfectly

        double MV=0;
        double amt;
        double interest=0;
        /*

        For RD, we have to calculate Maturity of each month and then add them.

         */
        for(int i=0; i<tenure; i++){

            year_tenure = local_tenure / 12;
            amt = amount * Math.pow((1 + rate2), 4*year_tenure);
            interest = (amt - amount);
            MV = MV + amt;
            local_tenure = local_tenure - 1;
            tableArea.addView(new TableRow(this, ""+(i+1), ""+Math.round(interest), ""+Math.round(MV)));


        }


    }


    //sipgraph
    public void drawSIPGraph(){

        if(tenure_type==1){
            tableHead.addView(new TableRow(this, "Month", "Return", "Value"));
        } else {
            tableHead.addView(new TableRow(this, "Month", "Return", "Value"));
        }

        if(tenure_type==1){
            tenure=tenure*12;
        }

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(amount*tenure, "Amount"));
        yvalues.add(new PieEntry(interest, "Return"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.primary_sip));
        colors.add(Color.parseColor("#FFA000"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_sip));

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14);
        pieData.setValueTypeface(poppins_bold);

        chart.setData(pieData);
        chart.setCenterText("Maturity Value: "+MV);
        chart.setCenterTextTypeface(poppins_bold);
        chart.animateX(500);
        chart.setCenterTextColor(getResources().getColor(R.color.primary_sip));
        desc.setText("Systematic Investment Return");
        desc.setTypeface(poppins_bold);
        chart.setDescription(desc);
        chart.getLegend().setTextColor(Color.WHITE);


        double rate2 = (rate) / 1200;
        double local_tenure = tenure;
        double year_tenure;

        //getting all the inputs perfectly

        double MVa=amount;
        double amt;
        double interest=0;

        /*

        For RD, we have to calculate Maturity of each month and then add them.


        for(int i=0; i<tenure; i++){

            year_tenure = local_tenure / 12;
            amt = amount * Math.pow((1 + rate2), 4*year_tenure);
            interest = (amt - amount);
            MV = MV + amt;
            local_tenure = local_tenure - 1;



        }
        */

        for(int i=0; i<tenure; i++){
            interest = MVa * rate2;
            MVa = MVa + interest;
            tableArea.addView(new TableRow(this, ""+(i+1), ""+Math.round(interest), ""+Math.round(MVa)));
            MVa = MVa + amount;

        }

    }


    //rdgraph
    public void drawRPGraph(){


        tableHead.addView(new TableRow(this, "Year", "Return", "Accumulation"));

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(total_invested, "Total Invested"));
        yvalues.add(new PieEntry(total_return, "Total Return"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.primary_rp));
        colors.add(Color.parseColor("#FF5722"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_rp));

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(14);
        pieData.setValueTypeface(poppins_bold);

        chart.setData(pieData);
        chart.setCenterText(String.format("Total Accumulation %.0f", amount));
        chart.setCenterTextTypeface(poppins_bold);
        chart.animateX(500);
        chart.setCenterTextColor(getResources().getColor(R.color.primary_rp));
        desc.setText("Retirement Amount");
        desc.setTypeface(poppins_bold);
        chart.setDescription(desc);
        chart.getLegend().setTextColor(Color.WHITE);

        double currentAmount=0;
        double final_rate = rate / 400;
        double local_tenure = tenure * 12;
        double yearly_tenure;
        double month_interest=0;
        double yearly_acc_interest=0;
        double amt;

        for(int i=0; i<tenure; i++){
            for(int j=0; j<12; j++){
                yearly_tenure = local_tenure / 12;
                amt = savings_per_month * Math.pow((1 + final_rate), 4*yearly_tenure);
                month_interest =  amt - savings_per_month;
                currentAmount = currentAmount + amt;
                local_tenure = local_tenure-1;
                yearly_acc_interest = yearly_tenure + month_interest;
            }
            tableArea.addView(new TableRow(this, ""+(i+1), ""+Math.round(yearly_acc_interest), ""+Math.round(currentAmount)));
            month_interest = 0;
        }






    }
}
