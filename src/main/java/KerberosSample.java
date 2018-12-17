import javax.security.auth.Subject;
import java.security.PrivilegedAction;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import java.net.URL;
import java.net.URLEncoder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;

class KerberosSample {

    private String principal;
    private String keytab;
    private String url;

    KerberosSample(String principal, String keytab, String url) {
        this.principal = principal;
        this.keytab = keytab;
        this.url = url;
    }

    public void run() {
        // create security configuration
        final SecurityConfiguration sc =
                new SecurityConfiguration(principal, keytab, true);

        // initialize login context
        LoginContext lc = null;
        try {
            lc = new LoginContext("KerberosSample", null, null, sc);
            lc.login();
        } catch (LoginException e) {
            System.out.println("authentication failed : " + e);
            return;
        }

        System.out.println("authentication successful");

        // run some code in Kerberos login context
        Subject subject = lc.getSubject();

        Subject.doAs(subject, new PrivilegedAction<Void>() {

            @Override
            public Void run() {

                runPrivileged();
                return null;

            }
        });
    }

    // test code
    void runPrivileged() {
        System.out.println("Executing code in kerberized login context");

        // build REST URL
        InputStream is = null;
        URL maprUrl = null;
        try {
            maprUrl = new URL(url);
        } catch (MalformedURLException e) {
            System.out.println("malformed URL generated internally: " + url + " , aborting.");
            return;
        }

        // execute REST call
        try {
            System.out.println("calling URL " + maprUrl.toString());
            is = maprUrl.openConnection().getInputStream();
        } catch (IOException e) {
            System.out.println("REST call error: " + e);
            return;
        }

        System.out.println("Success");
    }
}
