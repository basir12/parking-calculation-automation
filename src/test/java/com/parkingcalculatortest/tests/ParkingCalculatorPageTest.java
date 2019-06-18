package com.parkingcalculatortest.tests;

import com.dataprovider.DataProviderClass;
import com.parkingcalculatortest.BaseTestClass;
import com.parkingcalculatortest.ParkingCalculatorPage;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.parkingcalculatortest.ParkingCalculatorPage.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ParkingCalculatorPageTest extends BaseTestClass {
    private static final String ENTRY_TIME = "EntryTime";
    private static final String EXIT_TIME = "ExitTime";
    private static final String ENTRY_DATE = "EntryDate";
    private static final String EXIT_DATE = "ExitDate";


    ParkingCalculatorPage parkingCalculator = getParkingCalculationPage();

    @DataProvider
    public Object[][] dpParking(Method m) {
        return new Object[][] {
                { "STP", "11:50", "AM", "06/15/2019", "11:50", "PM", "06/15/2019", "$ 24.00", "(0 Days, 12 Hours, 0 Minutes)" },
                { "STP", "09:30", "AM", "06/14/2019", "06:10", "PM", "06/15/2019", "$ 44.00", "(1 Days, 8 Hours, 40 Minutes)" },
                { "STP", "11:50", "AM", "06/15/2019", "11:50", "PM", "06/14/2019", "ERROR! YOUR EXIT DATE OR TIME IS BEFORE YOUR ENTRY DATE OR TIME", "" },
                { "LTG", "11:50", "AM", "06/15/2019", "11:50", "PM", "06/15/2019", "$ 12.00", "(0 Days, 12 Hours, 0 Minutes)" },
                { "LTG", "09:30", "AM", "06/14/2019", "06:10", "PM", "06/15/2019", "$ 24.00", "(1 Days, 8 Hours, 40 Minutes)" },
                { "LTG", "11:50", "AM", "06/15/2019", "11:50", "PM", "06/14/2019", "$ 0.00", "(-1 Days, 12 Hours, 0 Minutes)" }
        };

    }

    @Test(dataProvider = "getDataFromCSV", dataProviderClass = DataProviderClass.class)
    public void calculateParkingTestCSV(String lot, String startTime, String periodOfTimeEntry, String startDate, String endTime, String periodOfTimeExit,String endDate,  String cost, String timeSpent ) throws InterruptedException {
        System.out.println(lot +" "+ startTime +" "+ periodOfTimeEntry +" "+ startDate +" "+ endTime  +" "+ periodOfTimeExit +" "+ endDate +" "+ cost +" "+ timeSpent);

        parkingCalculator.chooseLot(lot)
                .chooseDateTime(ENTRY_TIME, startTime)
                .choosePeriodTimeRB(ENTRY_TIME, periodOfTimeEntry)
                .setDate(ENTRY_DATE,startDate)
                .chooseDateTime(EXIT_TIME, endTime)
                .choosePeriodTimeRB(EXIT_TIME, periodOfTimeExit)
                .setDate(EXIT_DATE,endDate)
                .submit();


        assertThat(cost).isEqualTo(parkingCalculator.calculateCost());
        if(!cost.contains("ERROR")){
            assertThat(timeSpent).isEqualTo(parkingCalculator.obtainTimeConsumed());
        }
    }

    @Test(dataProvider = "dpParking")
    public void calculateParkingTest(String lot, String startTime, String periodOfTimeEntry, String startDate, String endTime, String periodOfTimeExit,String endDate,  String cost, String timeSpent ) throws InterruptedException {
        parkingCalculator.chooseLot(lot)
                            .chooseDateTime(ENTRY_TIME, startTime)
                            .choosePeriodTimeRB(ENTRY_TIME, periodOfTimeEntry)
                            .setDate(ENTRY_DATE,startDate)
                            .chooseDateTime(EXIT_TIME, endTime)
                            .choosePeriodTimeRB(EXIT_TIME, periodOfTimeExit)
                            .setDate(EXIT_DATE,endDate)
                            .submit();

        assertThat(cost).isEqualTo(parkingCalculator.calculateCost());
        if(!cost.contains("ERROR")){
            assertThat(timeSpent).isEqualTo(parkingCalculator.obtainTimeConsumed());
        }


    }
}
