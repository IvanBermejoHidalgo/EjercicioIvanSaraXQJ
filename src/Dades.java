public class Dades {
    double ed50CoordX;
    double ed50CoordY;
    double etrs89CoordX;
    double etrs89CoordY;
    double longitud;
    double latitud;
    String icon;
    String tooltip;
    String url;

    public Dades(double ed50CoordX, double ed50CoordY, double etrs89CoordX, double etrs89CoordY, double longitud, double latitud, String icon, String tooltip, String url) {
        this.ed50CoordX = ed50CoordX;
        this.ed50CoordY = ed50CoordY;
        this.etrs89CoordX = etrs89CoordX;
        this.etrs89CoordY = etrs89CoordY;
        this.longitud = longitud;
        this.latitud = latitud;
        this.icon = icon;
        this.tooltip = tooltip;
        this.url = url;
    }

    public double getEd50CoordX() {
        return ed50CoordX;
    }

    public void setEd50CoordX(double ed50CoordX) {
        this.ed50CoordX = ed50CoordX;
    }

    public double getEd50CoordY() {
        return ed50CoordY;
    }

    public void setEd50CoordY(double ed50CoordY) {
        this.ed50CoordY = ed50CoordY;
    }

    public double getEtrs89CoordX() {
        return etrs89CoordX;
    }

    public void setEtrs89CoordX(double etrs89CoordX) {
        this.etrs89CoordX = etrs89CoordX;
    }

    public double getEtrs89CoordY() {
        return etrs89CoordY;
    }

    public void setEtrs89CoordY(double etrs89CoordY) {
        this.etrs89CoordY = etrs89CoordY;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Punt{" +
                "ED50 Coord X=" + ed50CoordX +
                ", ED50 Coord Y=" + ed50CoordY +
                ", ETRS89 Coord X=" + etrs89CoordX +
                ", ETRS89 Coord Y=" + etrs89CoordY +
                ", Longitud=" + longitud +
                ", Latitud=" + latitud +
                ", Icon='" + icon + '\'' +
                ", Tooltip='" + tooltip + '\'' +
                ", URL='" + url + '\'' +
                '}';
    }
}
