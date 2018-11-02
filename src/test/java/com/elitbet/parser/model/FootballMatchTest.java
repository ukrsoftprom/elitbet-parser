package com.elitbet.parser.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class FootballMatchTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void toURL() {
        FootballMatch footballMatch = new FootballMatch();
        footballMatch.setTournamentName("tournamentName");
        footballMatch.setEventType("Football Match");
        footballMatch.setEventId("eventId");
        footballMatch.setHomeTeam("homeTeam");
        footballMatch.setGuestTeam("guestTeam");
        footballMatch.setHomeCoefficient(1.0);
        footballMatch.setGuestCoefficient(1.0);
        footballMatch.setDrawCoefficient(1.0);
        footballMatch.setAccessToken("1");
        long timestamp = 1000;
        Date date = new Date(timestamp);
        footballMatch.setDate(date);
        String expected = "/events/create?access_token=1&start_timestamp=1000&parameters=home_name:homeTeam;away_name=guestTeam&tournament=tournamentName&coefficients=1:1.0;2:1.0;X:1.0&id=eventId&event_type=Football+Match";
        assertEquals(expected, footballMatch.toURL());
    }
}
