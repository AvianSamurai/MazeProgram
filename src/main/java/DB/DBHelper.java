package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DBHelper {
    /**
     * Returns a 2D array of rows of strings where the search string matches either the ID, Name, or Author
     * rows are formatted as following
     * | id | name | author | creation_date | last_modified |
     *
     * The search string will be trimmed of any white space or zero space characters and the search will not be
     * case-sensitive
     *
     * this does not contain the maze data, to get the maze data you should use the id as names do not have to
     * be unique
     *
     * @param db An active database object
     * @param searchString The string to search the database with
     * @return A 2D array of strings containing the results
     */
    public static String[][] GetMazeListBySearchString(MazeDB db, String searchString) {
        String searchStr = searchString.trim().toLowerCase();
        ResultSet r;
        try {
            r = db.Query("SELECT id, name, author_name, creation_date, last_modified FROM saved_mazes");
            ArrayList<String[]> data = new ArrayList<String[]>();
            while(r.next()) {
                if(r.getString(1).equals(searchStr) ||
                        r.getString(2).toLowerCase().contains(searchStr) ||
                        r.getString(3).toLowerCase().contains(searchStr)) {
                    data.add(new String[]{r.getString(1), r.getString(2),
                            r.getString(3), r.getString(4), r.getString(5)});
                }
            }
            return data.toArray(String[][]::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}