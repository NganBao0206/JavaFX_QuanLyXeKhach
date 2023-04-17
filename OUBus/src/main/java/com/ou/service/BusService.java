/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.utils.ValidResult;
import com.ou.conf.JdbcUtils;
import com.ou.pojo.Bus;
import com.ou.pojo.BusTrip;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author yuumm
 */
public class BusService {

    public List<Bus> getBuses() throws SQLException {

        List<Bus> buses = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bus");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String licensePlates = rs.getNString("LicensePlates");
                buses.add(new Bus(id, licensePlates));
            }
        }

        return buses;
    }


    public List<Bus> getAvailableBuses(BusTrip busTrip) throws SQLException {
        BusTripService bts = new BusTripService();
        List<Bus> buses = getBuses();

        List<Bus> bestChoices = new ArrayList<>();
        List<Bus> goodChoices = new ArrayList<>();
        List<Bus> choices = new ArrayList<>();
        List<Bus> badChoices = new ArrayList<>();
        List<Bus> worstChoices = new ArrayList<>();

        for (Bus b : buses) {
            busTrip.setBusId(b.getId());
            ValidResult beforeTrip = bts.isValidBusTripWithBeforeTrip(busTrip);
            ValidResult afterTrip = bts.isValidBusTripWithAfterTrip(busTrip);
            if (!bts.isValidBusTrip(busTrip) || beforeTrip.getResult() == -1 || afterTrip.getResult() == -1) {

            } else if (beforeTrip.getResult() == 0 || afterTrip.getResult() == 0) 
                worstChoices.add(b);
            else if (beforeTrip.getTrip().getDepartureId() == -1 && afterTrip.getTrip().getDepartureId() == -1)
                choices.add(b);
            else if (beforeTrip.getTrip().getDestinationId() != busTrip.getDepartureId() || afterTrip.getTrip().getDepartureId() != busTrip.getDestinationId())
                badChoices.add(b);
            else if (beforeTrip.getTrip().getDestinationId() == busTrip.getDepartureId() && afterTrip.getTrip().getDepartureId() == busTrip.getDestinationId())
                bestChoices.add(b);
            else if (beforeTrip.getTrip().getDestinationId() == busTrip.getDepartureId() || afterTrip.getTrip().getDepartureId() == busTrip.getDestinationId())
                goodChoices.add(b);

        }

        List<Bus> result = new ArrayList<>();

        Collections.sort(bestChoices, new Comparator<Bus>() {
            @Override
            public int compare(Bus b1, Bus b2) {
                try {
                    busTrip.setBusId(b1.getId());
                    ValidResult beforeTrip1 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip1 = bts.isValidBusTripWithAfterTrip(busTrip);
                    long durationB1 = Duration.between(beforeTrip1.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                    long durationA1 = Duration.between(afterTrip1.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                    long total1 = durationB1 + durationA1;

                    busTrip.setBusId(b2.getId());
                    ValidResult beforeTrip2 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip2 = bts.isValidBusTripWithAfterTrip(busTrip);
                    long durationB2 = Duration.between(beforeTrip2.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                    long durationA2 = Duration.between(afterTrip2.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                    long total2 = durationB2 + durationA2;
                    return total1 < total2 ? -1 : 1;

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Collections.sort(goodChoices, new Comparator<Bus>() {
            @Override
            public int compare(Bus b1, Bus b2) {
                try {
                    busTrip.setBusId(b1.getId());

                    ValidResult beforeTrip1 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip1 = bts.isValidBusTripWithAfterTrip(busTrip);
                    long total1 = 0;
                    if (beforeTrip1.getTrip() != null)
                    {
                        total1 = Duration.between(beforeTrip1.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                    }
                    else {
                        total1 = Duration.between(afterTrip1.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                    }


                    busTrip.setBusId(b2.getId());
                    ValidResult beforeTrip2 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip2 = bts.isValidBusTripWithAfterTrip(busTrip);
                    long total2 = 0;
                    if (beforeTrip1.getTrip() != null)
                    {
                        total2 = Duration.between(beforeTrip2.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                    }
                    else {
                        total2 = Duration.between(afterTrip2.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                    }

                    return total1 < total2 ? -1 : 1;

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Collections.sort(badChoices, new Comparator<Bus>() {
            @Override
            public int compare(Bus b1, Bus b2) {
                try {
                    busTrip.setBusId(b1.getId());

                    ValidResult beforeTrip1 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip1 = bts.isValidBusTripWithAfterTrip(busTrip);

                    int midTimeB1 = beforeTrip1.getMidRoute() != null ? beforeTrip1.getMidRoute().getTotalTime() : 0;
                    int midTimeA1 = afterTrip1.getMidRoute() != null ? afterTrip1.getMidRoute().getTotalTime() : 0;
                    int totalMidTime1 = midTimeB1 + midTimeA1;

                    busTrip.setBusId(b2.getId());
                    ValidResult beforeTrip2 = bts.isValidBusTripWithBeforeTrip(busTrip);
                    ValidResult afterTrip2 = bts.isValidBusTripWithAfterTrip(busTrip);

                    int midTimeB2 = beforeTrip2.getMidRoute() != null ? beforeTrip2.getMidRoute().getTotalTime() : 0;
                    int midTimeA2 = afterTrip2.getMidRoute() != null ? afterTrip2.getMidRoute().getTotalTime() : 0;
                    int totalMidTime2 = midTimeB2 + midTimeA2;

                    int result = totalMidTime1 - totalMidTime2;
                    if (result != 0)
                        return result;
                    else {
                        long totalB1 = 0;
                        long totalA1 = 0;
                        if (beforeTrip1.getTrip().getDepartureId() != -1)
                        {
                            totalB1 = Duration.between(beforeTrip1.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                        }
                        if (afterTrip1.getTrip().getDepartureId() != -1) {
                            totalA1 = Duration.between(afterTrip1.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                        }
                        long total1 = totalB1 + totalA1;

                        long totalB2 = 0;
                        long totalA2 = 0;
                        if (beforeTrip2.getTrip().getDepartureId() != -1)
                        {
                            totalB2 = Duration.between(beforeTrip2.getMinDate(), busTrip.getDepartureTime()).toMinutes();
                        }
                        if (afterTrip2.getTrip().getDepartureId() != -1) {
                            totalA2 = Duration.between(afterTrip2.getMinDate(), busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime())).toMinutes();
                        }
                        long total2 = totalB2 + totalA2;

                        return total1 < total2 ? -1 : 1;
                    }


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        result.addAll(bestChoices);
        result.addAll(goodChoices);
        result.addAll(choices);
        result.addAll(badChoices);
        result.addAll(worstChoices);
        return result;
    }
}
