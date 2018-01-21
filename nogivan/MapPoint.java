package nogivan;

/**
 * Diese Klasse implementiert einen Kartenpunkt. Ein
 * Kartenpunkt hat einen Position in Form eines Länge-
 * und Breitengrades.
 */
public class MapPoint
{
  /**
   * Der Breitengrad
   */
  private double lat;

  public double getLat()
  {
    return lat;
  }

  /**
   * Der Längengrad
   */
  private double lon;

  public double getLon()
  {
    return lon;
  }

  public MapPoint(double lat, double lon)
  {
    this.lat = lat;
    this.lon = lon;
  }

  /**
   * Diese Methode berechnet den Abstand dieses Kartenpunktes
   * zu einem anderen Kartenpunkt.
   *
   * @param other der andere Kartenpunkt
   * @return der Abstand in Metern
   */

  public int distance(MapPoint other)
  {
    double R = 6371e3; // metres
    double φ1 = Math.toRadians(lat);
    double φ2 = Math.toRadians(other.getLat());
    double Δφ = Math.toRadians((other.getLat() - lat));
    double Δλ = Math.toRadians((other.getLon() - lon));

    double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
        Math.cos(φ1) * Math.cos(φ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    double d = R * c;
    return (int) d;
  }

  @Override
  public String toString()
  {
    return "lat = " + lat + ", lon = " + lon;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
