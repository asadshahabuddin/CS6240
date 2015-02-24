(1) The current project runs for the minified input file '*.csv' (* signifies 
    wildcard characters).

(2) In order to adapt it to the original input file (~14GB), you would need to
    add the U.S. NATIONAL holidays for each year to the static block. I have
    done that for 2012. For this, you need to find out all the years for which 
    data is provided in our large input file, find a list of U.S. NATIONAL 
    holidays for them from www.timeanddate.com and append to the aforementioned 
    block. Please do this on your local machine and do not commit anything until
    it is thoroughly tested. Alternatively, you can maintain as a separate file 
    in the same package. Name it App%.java (% is a single wildcard character).

(3) The output value never comes to 1. According to my estimation, it is because
    I have written the program in such a way that if the probability of getting 
    0 (zero) as output is greater than or equal to 0.5 (50%), the output values 
    invariably becomes zero. This is a terrible approximation at best and needs 
    to be discussed.

(4) We need to convert my single-file program to a MapReduce application.

(5) Testing and documentation are at 0%.

(6) The application needs to run from console and not just Eclipse.

(7) Perform a 'Find and Replace' for '.csv' and you will find all points where
    I have hard-coded file names. This is where you make changes.
