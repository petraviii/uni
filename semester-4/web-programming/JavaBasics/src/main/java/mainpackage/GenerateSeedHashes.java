package mainpackage;

/**
 * One-time utility to print out salt/hash pairs for seeding the
 * database with hashed passwords. Not part of the final application —
 * run once, copy the output into SQL, then this class can be deleted.
 *
 * @author Petros Travlos P21173
 */
public class GenerateSeedHashes {

    public static void main(String[] args) {
        // Add any username/password pairs you want hashes for here.
        String[][] users = {
                {"mpapas", "pass123"},
                {"gkostas", "pass123"},
                {"avasili", "pass123"},
                {"sregistry", "secpass1"},
                {"sadmin", "secpass2"},
                {"jdoe", "stud123"},
                {"mklein", "stud123"},
                {"bsmith", "stud123"},
                {"acohen", "stud123"}
        };

        for (String[] user : users) {
            String username = user[0];
            String password = user[1];
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(password, salt);

            System.out.println("-- " + username + " (password: " + password + ")");
            System.out.println("UPDATE professors SET salt = '" + salt + "', password_hash = '" + hash + "' WHERE username = '" + username + "';");
            System.out.println("-- (copy the correct line below into the right table — see note)");
            System.out.println();
        }
    }
}