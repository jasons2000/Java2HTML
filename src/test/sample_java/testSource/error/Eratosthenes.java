/*
 *   Copyright (c) 2001 Peter Luschny
 *
 *   This code may not be distributed  by yourself except in binary
 *   form,  incorporated into a java .class file.  You may use this
 *   code freely for personal purposes, but you may not incorporate
 *   it into any  commercial product without  express permission of
 *   Peter Luschny in writing.
 *
 *   This notice and attribution to Peter Luschny may not be removed.
 *
 *   Comments and bug reports are welcome.
 *   Mailto: codemaster@javajungel.de
 */

package sample_java.testSource.error;

import javajungle.math.util.IRange;
import java.io.PrintWriter ;
import java.io.FileWriter ;
import java.io.BufferedWriter;
import java.io.IOException ;

/**
 * Sieve
 *
 * @author Peter Luschny
 * @version 2001-05-12
 */

public class Eratosthenes implements PrimeSieve
{
    private final int[] primes;
    private final IRange range;
    private int numberOfPrimes;

    /**
     * Constructor declaration
     *
     * @param n the upper bound of the range to be sieved
     */

    public Eratosthenes(int n)
    {
        primes = new int[GetPiHighBound(n)];
        range = new IRange(1, n);

        makePrimeList(n);
    }

    /**
     * Primzahlen Sieb Eratosthenes ( 276-194 v.Chr. )
     * Diese Variante laesst die Vielfachen von 2 und 3 von
     * vornherein aussen vor, d.h. der kleinste Werte ist
     * auf 5 zu mappen.
     * In diesem Sieb kommt keine Multiplikationsoperation vor.
     *
     * @param composite nach Aufruf der Funktion enthaelt das
     * Datenfeld alle zusammengesetzten Zahlen in [5,n]
     * ausser den Vielfachen von 2 und 3
     */

    private void sieveOfEratosthenes(boolean[] composite)
    {
        int d1 = 8;
        int d2 = 8;
        int p1 = 3;
        int p2 = 7;
        int s = 7;
        int s2 = 3;
        int n = 0;
        int Len = composite.length;
        boolean toggle = false;

        while (s < Len)                 // --  scan sieve
        {
            if (!composite[n++])        // --  if a prime is found
            {                           // --  cancel its multiples
                int inc = p1 + p2;

                for (int k = s; k < Len; k += inc)
                {
                    composite[k] = true;
                }

                for (int k = s + s2; k < Len; k += inc)
                {
                    composite[k] = true;
                }
            }

            if (toggle = !toggle)
            {
                s += d2;
                d1 += 16;
                p1 += 2;
                p2 += 2;
                s2 = p2;
            }
            else
            {
                s += d1;
                d2 += 8;
                p1 += 2;
                p2 += 6;
                s2 = p1;
            }
        }
    }

    /**
     * Transformiert das Sieb des Eratosthenes in die Folge der
     * (haengengebliebenen) Primzahlen.
     *
     * @param n obere Grenze des Siebs 
     */

    private void makePrimeList(int n)
    {
        boolean[] composite = new boolean[n / 3];

        sieveOfEratosthenes(composite);

        int[] primes = this.primes;     // -- on stack for eff.
        boolean toggle = false;
        int p = 5, i = 0, j = 2;

        primes[0] = 2;
        primes[1] = 3;

        while (p <= n)
        {
            if (!composite[i++])
            {
                primes[j++] = p;
            }

            p += (toggle = !toggle) ? 2 : 4;
        }

        numberOfPrimes = j;
    }

    /**
     * Get the n-th prime number.
     *
     * @param n the index of the prime number 
     *
     * @return the n-th prime number
     */

    public int getNthPrime(int N)
    {
        if ((0 < N) && (N <= numberOfPrimes))
        {
            return primes[N - 1];
        }
        else
        {
            throw new IndexOutOfBoundsException(Integer.toString(N));
        }
    }

    /**
     * Checks if a given number is prime.
     * 
     * @param the number to be checked
     *
     * @return true iff the given number is prime
     */

    public boolean isPrime(int cand)
    {
        if (range.contains(cand))
        {
            return (new Enumerator(this, cand)).hasMorePrimes();
        }
        else
        {
            throw new IndexOutOfBoundsException(Integer.toString(cand));
        }
    }

    /**
     * GetPiHighBound
     *
     * @param upper bound of the primes.
     *
     * @return simple estimate of the number of primes <= n.
     */

    static private int GetPiHighBound(long n)
    {
        if (n < 17)
        {
            return 6;
        }
        else
        {
            return (int) Math.floor(((double) n) / (Math.log(n) - 1.5));
        }
    }

    /**
     * The default enumeration of the full sieve.
     *
     * @return the prime number enumeration
     */

    public PrimeEnumerator getPrimeEnumerator()
    {
        return new Enumerator(this);
    }

    /**
     * Gives the enumeration of the prime numbers in the given intervall.
     *
     * @param low lower bound of the enumeration (excluded)
     * @param high upper bound of the enumeration (included)
     *
     * @return the prime number enumeration
     */

    public PrimeEnumerator getPrimeEnumerator(int low, int high)
    {
        return new Enumerator(this, new IRange(low, high));
    }

    /**
     * Gives the enumeration of the prime numbers in the given range.
     *
     * @param range the range of the enumeration
     *
     * @return the prime number enumeration
     */

    public PrimeEnumerator getPrimeEnumerator(IRange range)
    {
        return new Enumerator(this, range);
    }


    ////////////////////////// inner class ///////////////////////////

    /**
     * Enumerator
     *
     * @author Peter Luschny
     * @version 2001-05-12
     */

    private class Enumerator implements PrimeEnumerator
    {
        private final Eratosthenes sieve;
        private final IRange sieveRange;
        private final IRange primeRange;
        private final int indexStart;
        private final int indexBound;
        private int indexCurrent;

        /**
         * Erzeugt die Iterationsfunktionen fuer das uebergebene Sieb.
         *
         * @param sieve
         */

        public Enumerator(Eratosthenes sieve)
        {
            this.sieve = sieve;

            indexCurrent = indexStart = 0;
            indexBound = sieve.numberOfPrimes;
            sieveRange = sieve.range;
            primeRange = new IRange(1, indexBound);
        }

        /**
         * Erzeugt die Iterationsfunktionen f�r ein Punktintervall.
         * Eine etwas ungewoehnliche Abz�hlung, zugegeben. Aber
         * 'Eratosthenes' implementiert mit ihr den Test auf Primalit�t
         * einer Zahl. Denn eine Zahl cand ist genau dann prim, wenn im
         * Intervall  (cand-1, cand] eine Primzahl liegt.
         *
         * @param sieve das Primzahlensieb
         * @param cand integer gedeutet als Punktintervall
         */

        public Enumerator(Eratosthenes sieve, int cand)
        {
            this.sieve = sieve;
            // NOTE BENE: in this case no primeRange is defined! This case
            // describes a singelton, but an IRange includes at least 2 elements.
            // Also note, that this Enumerator is not made public via PrimeSieve.
            // It is used only to test primality. It is a private constructor
            // only for Eratosthenes.
            primeRange = sieveRange = null ;

            indexCurrent = indexStart = indexOf(cand - 1, 0, sieve.numberOfPrimes);
            indexBound = sieve.primes[indexStart] == cand ? indexStart + 1 : indexStart;
        }

        /**
         * Erzeugt die Iterationsfunktionen f�r ein Teilintervall
         * der berechneten Primzahlen.
         * Untere Grenze ist aus-, obere Grenze wird eingeschlossen.
         * Exception wird geworfen, wenn NICHT gilt:
         * sieveLow <= lowBound < highBound <= sieveHigh
         *
         * @param lowBound untere Intervallgrenze (ausgeschlossen!)
         * @param highBound obere Intervallgrenze (eingeschlossen!)
         * @throws IndexOutOfBoundsException
         */

        public Enumerator(Eratosthenes sieve, IRange sieveRange)
        {
            this.sieve = sieve;
            this.sieveRange = sieveRange;

            if (!sieve.range.contains(sieveRange))
            {
                throw new IndexOutOfBoundsException(sieveRange.toString());
            }

            indexStart = indexOf(sieveRange.getLow(), 0, sieve.numberOfPrimes);
            indexBound = indexOf(sieveRange.getHigh(), indexStart, sieve.numberOfPrimes);
            indexCurrent = indexStart;
            primeRange = new IRange(indexStart + 1, indexBound);
        }

        /**
         * Returns the next prime number in the enumeration.
         *
         * @return the next prime number in the enumeration
         */

        public int getNextPrime()
        {
            return sieve.primes[ indexCurrent++ ];
        }

        /**
         * Checks the current status of the finite enumeration
         *
         * @return true iff there are more prime numbers to be enumerated
         */

        public boolean hasMorePrimes()
        {
            return indexCurrent < indexBound;
        }

        /**
         * Resets the enumeration.
         *
         */

        public void reset()
        {
            indexCurrent = indexStart;
        }

        /**
         * Identifiziert den Index einer Primzahl.
         * Verwendet dazu eine (modifizierte!) bin�re Suche.
         *
         * @param value gegebene  Primzahl
         * @param low unterer Schranke f�r den Index, muss ein g�ltiger Index sein
         * @param high oberer Schranke f�r den Index, muss KEIN g�ltiger Index sein!
         *
         * @return Index der Primzahl
         */

        private int indexOf(int value, int low, int high)
        {
            int[] data = sieve.primes;

            while (low < high)
            {
                int mid = (low + high) / 2;

                if (data[ mid ] < value)
                {
                    low = mid + 1;
                }
                else
                {
                    high = mid;
                }
            }

            if (data[ low ] == value)
            {
                low++;
            }

            return low;
        }

        /**
         * Gives the prime numbers in the enumeration as an array.
         *
         * @return an array of prime numbers representing the enumeration
         */

        public int[] toArray()
        {
            int primeCard = primeRange.size();
            int[] primeList = new int[ primeCard ];

            System.arraycopy(sieve.primes, indexStart, primeList, 0, primeCard);

            return primeList;
        }

        /**
         * Computes the number of primes in the enumeration range.
         *
         * @return cardinality of primes in enumeration range
         */

        public int getNumberOfPrimes()
        {
            return primeRange.size();
        }

        /**
         * Gives the intervall (a,b] of the sieve.
         *
         * @return sieved intervall 
         */

        public IRange getSieveRange()
        {
            return (IRange) sieveRange.clone();
        }

        /**
         * Gives the range of the indices of the prime numbers in the enumeartion.
         *
         * @return range of indices
         */

        public IRange getPrimeRange()
        {
            return (IRange) primeRange.clone();
        }

        /**
         * Writes the primes enumeration to a file.
         *
         */

        public void filePrimes ( String fileName )
        {
            try {
                PrintWriter primeReport =
                        new PrintWriter(
                        new BufferedWriter(
                        new FileWriter(fileName)));
                writePrimes( primeReport ) ;
                primeReport.close();
            } catch (IOException e) {
                System.err.println( e.toString()) ;
            }
        }

        /**
         *  Hier gibt es ein kleines Problem.
         *  Zum Rechnen ist es besser, mit Intervallen der Form
         *  (a,b] zu rechnen (a exklusiv, b inklusiv), da man dann
         *  sehr einfach Intervalle aneinander heften kann, denn es gilt
         *  (a,b](b,c]=(a,c]
         *
         *  Aus dem Kontext geloest in eine Datei geschrieben, ist die
         *  Gefahr gross, dass es beim unbefangenen Leser zum Missverstaendnis
         *  fuehrt.
         *
         *  Deshalb aendern wir hier die externe Dokumentation, und geben
         *  alle Intervalle in der Form [a,b] nach aussen, also alle
         *  Grenzen sind inklusive, was dem "Default" der "Erwartung" entspricht.
         */

        private void writePrimes(PrintWriter file )
        {
            IRange sieveRange = (IRange) this.sieveRange.clone();
            sieveRange.setLow(sieveRange.getLow()+1);

            file.println("SieveRange   " + sieveRange.toString() + " : " + sieveRange.size());
            file.println("PrimeRange   " + primeRange.toString() + " : " + primeRange.size());
            file.println("PrimeDensity " + (double)primeRange.size()/(double)sieveRange.size());

            int primeOrdinal = indexStart;
            int primeLow  = sieve.primes[ indexCurrent ];
            int lim = (primeLow / 100) * 100 ;

            while( hasMorePrimes() )
            {
                int prime = getNextPrime();
                primeOrdinal++ ;
                if( prime >= lim )
                {
                    lim += 100 ;
                    file.println();
                    file.print("<");
                    file.print( primeOrdinal );
                    file.print(".> ");
                }
                file.print(prime);
                file.print(" ");
            }
            file.println();
        }

    }   // endOfEnumerator
}   // endOfEratosthenes

