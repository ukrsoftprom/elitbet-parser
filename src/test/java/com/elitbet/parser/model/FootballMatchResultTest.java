package com.elitbet.parser.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class FootballMatchResultTest {

    @Test
    public void toURL() {
        FootballMatchResult footballMatchResult = new FootballMatchResult();
        footballMatchResult.setTournamentName("tournamentName");
        footballMatchResult.setEventType("Football Match");
        footballMatchResult.setEventId("eventId");
        footballMatchResult.setHomeTeam("homeTeam");
        footballMatchResult.setGuestTeam("guestTeam");
        footballMatchResult.setGuestTeamGoals(1);
        footballMatchResult.setHomeTeamGoals(1);
        footballMatchResult.setAccessToken("1");
        footballMatchResult.setStatus("Finished");
        long timestamp = 1000;
        Date date = new Date(timestamp);
        footballMatchResult.setDate(date);
        String expected = "access_token=1&start_timestamp=1000&parameters=home_name:homeTeam;away_name:guestTeam;home_goals:1;away_goals:1&tournament=tournamentName&status=Finished&id=eventId";
        assertEquals(expected, footballMatchResult.toURL());
    }
}
