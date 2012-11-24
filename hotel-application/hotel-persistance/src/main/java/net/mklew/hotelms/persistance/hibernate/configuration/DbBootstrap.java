package net.mklew.hotelms.persistance.hibernate.configuration;

import net.mklew.hotelms.domain.booking.reservation.rates.*;
import net.mklew.hotelms.domain.room.*;
import org.hibernate.Session;
import org.jcontainer.dna.Logger;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.picocontainer.Startable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Populates database with some data upon startup.
 * One place to hardcore everything
 *
 * It should not be used for production!
 *
 * @author Marek Lewandowski <marek.m.lewandowski@gmail.com>
 * @since 11/11/12
 *        Time: 8:11 PM
 */
public class DbBootstrap implements Startable
{
    final private Logger logger;
    final private HibernateSessionFactory hibernateSessionFactory;

    public DbBootstrap(Logger logger, HibernateSessionFactory hibernateSessionFactory)
    {
        this.logger = logger;
        this.hibernateSessionFactory = hibernateSessionFactory;
    }

    @Override
    public void start()
    {
        bootstrap();
    }

    private void bootstrap()
    {
        logger.debug("Started bootstrapping database");
        Session session = hibernateSessionFactory.getCurrentSession();

        // bootstrapping data

        Collection<RoomType> types = new ArrayList<>();
        RoomType luxury = new RoomType("luxury");
        RoomType cheap = new RoomType("cheap");
        RoomType niceOne = new RoomType("nice one");

        types.addAll(Arrays.asList(luxury, cheap, niceOne));
        Collection<Room> rooms;

        Money standardPrice = Money.parse("USD 100");
        Money upchargeExtraPerson = Money.parse("USD 110");
        Money upchargeExtraBed = Money.parse("USD 120");
        RackRate rackRate = new RackRate(standardPrice, upchargeExtraPerson, upchargeExtraBed, null);
        RackRate rackRate1 = new RackRate(standardPrice.plus(10), upchargeExtraPerson.plus(10), upchargeExtraBed.plus(10), null);
        RackRate rackRate2 = new RackRate(standardPrice.plus(20), upchargeExtraPerson.plus(20), upchargeExtraBed.plus(20), null);
        RackRate rackRate3 = new RackRate(standardPrice.plus(30), upchargeExtraPerson.plus(30), upchargeExtraBed.plus(30), null);
        RackRate rackRate4 = new RackRate(standardPrice.plus(40), upchargeExtraPerson.plus(40), upchargeExtraBed.plus(40), null);
        RackRate rackRate5 = new RackRate(standardPrice.plus(50), upchargeExtraPerson.plus(50), upchargeExtraBed.plus(50), null);

        Room L100 = new Room("L", new RoomName("100"), luxury, rackRate, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 1, new Occupancy(3, 4));
        Room L101 = new Room("L", new RoomName("101"), luxury, rackRate1, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 1, new Occupancy(2, 3));
        Room L102 = new Room("L", new RoomName("102"), luxury, rackRate2, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 3, new Occupancy(2,4));
        Room C103 = new Room("C", new RoomName("103"), cheap, rackRate3, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 4, new Occupancy(4, 10));
        Room C104 = new Room("C", new RoomName("104"), cheap, rackRate4, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 5, new Occupancy(6, 12));
        Room N105 = new Room("N", new RoomName("105"), niceOne, rackRate5, HousekeepingStatus.CLEAN, RoomAvailability.AVAILABLE, 2, new Occupancy(2, 5));

        rooms = Arrays.asList(L100, L101, L102, C103, C104, N105);

        Collection<Rate> rates;

        Season season = new BasicSeason("winter special", new AvailabilityPeriod(DateTime.now(), DateTime.now().plusDays(90), true));
        Season season2 = new BasicSeason("christmas special", new AvailabilityPeriod(DateTime.now(), DateTime.now().plusDays(30), true));

        Rate rate1_L100 = new SeasonRate(Money.parse("USD 50"), Money.parse("USD 60"), Money.parse("USD 70"), L100, season);
        Rate rate2_L100 = new SeasonRate(Money.parse("USD 20"), Money.parse("USD 60"), Money.parse("USD 70"), L100, season2);
        Rate rate1_L101 = new SeasonRate(Money.parse("USD 60"), Money.parse("USD 70"), Money.parse("USD 60"), L101, season);
        Rate rate2_L101 = new SeasonRate(Money.parse("USD 20"), Money.parse("USD 70"), Money.parse("USD 60"), L101, season2);
        Rate rate1_L102 = new SeasonRate(Money.parse("USD 60"), Money.parse("USD 70"), Money.parse("USD 60"), L102, season);
        Rate rate2_L102 = new SeasonRate(Money.parse("USD 20"), Money.parse("USD 70"), Money.parse("USD 60"), L102, season2);
        Rate rate1_C103 = new SeasonRate(Money.parse("USD 40"), Money.parse("USD 70"), Money.parse("USD 60"), C103, season);
        Rate rate2_C103 = new SeasonRate(Money.parse("USD 30"), Money.parse("USD 70"), Money.parse("USD 60"), C103, season2);
        Rate rate1_C104 = new SeasonRate(Money.parse("USD 70"), Money.parse("USD 70"), Money.parse("USD 60"), C104, season);
        Rate rate2_C104 = new SeasonRate(Money.parse("USD 50"), Money.parse("USD 70"), Money.parse("USD 60"), C104, season2);
        Rate rate1_N105 = new SeasonRate(Money.parse("USD 80"), Money.parse("USD 70"), Money.parse("USD 60"), N105, season);
        Rate rate2_N105 = new SeasonRate(Money.parse("USD 90"), Money.parse("USD 70"), Money.parse("USD 60"), N105, season2);

        Collection<Season> seasons = Arrays.asList(season, season2);
        rates = Arrays.asList(rate1_L100, rate2_L100, rate1_L101, rate2_L101, rate1_L102, rate2_L102, rate1_C103,
                rate2_C103, rate1_C104, rate2_C104, rate1_N105, rate2_N105);

        // bootstrapping data
        session.beginTransaction();
        logger.debug("adding room types:");
        for (RoomType type : types)
        {
            session.save(type);
            logger.debug("room type: " + type.toString());
        }
        logger.debug("adding rooms");
        for (Room room : rooms)
        {
            logger.debug("room: " + room.toString());
            session.save(room);
        }

        logger.debug("adding seasons");
        for(Season s : seasons)
        {
            logger.debug("season: " + s.toString());
            session.save(s);
        }

        logger.debug("adding season rates");
        for(Rate rate : rates)
        {
            logger.debug("rate: " + rate.toString());
            session.save(rate);
        }

        session.getTransaction().commit();
        logger.debug("Finished bootstrapping database");
    }

    @Override
    public void stop()
    {
    }
}