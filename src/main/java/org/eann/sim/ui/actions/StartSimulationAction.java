package org.eann.sim.ui.actions;

import org.eann.sim.ui.MainFrame;

import java.awt.event.ActionEvent;

/**
 * Created by martin on 22.03.17.
 */
public class StartSimulationAction extends AbstractWorldPanelAction {

    public StartSimulationAction(MainFrame mainframe) {
        super("Start Simulation", mainframe);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.mainframe.getSimulation().setPauseSimulation(false);
    }
}
