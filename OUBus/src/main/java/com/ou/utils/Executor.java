/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.utils;

import com.ou.pojo.Ticket;
import com.ou.service.TicketService;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Hi
 */
public class Executor {
    private static Executor instance;
    private ScheduledExecutorService executor;


    private Executor() {
        TicketService ts = new TicketService();
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                List<Ticket> invalidTickets = ts.getInvalidTickets();
                System.out.println();
                for (Ticket t : invalidTickets) {
                    ts.deleteTicket(t.getId());
                }
            } catch (SQLException ex) {
                Logger.getLogger(TicketService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public static Executor getInstance() {
        if(instance == null) {
            instance = new Executor();
        }
        return instance;
    }

    public void shutDownExecutor() {
        executor.shutdown();
    }
}
