# uk-food-hygiene

Show the food hygiene ratings attributed to all London restaurants, by borough.

## Prerequisites

You will need:
- [Leiningen][1] 2.0 or above installed;
- [PostgreSQL][2];
- [PostGIS][3].

[1]: https://github.com/technomancy/leiningen
[2]: http://www.postgresql.org
[3]: http://postgis.net

## Running

With `postgresql` running, create the database:

    $ createdb uk_food_hygiene

Note that you might have to enable the `postgis` extension. If that's the case,
login to postgres as a superuser and create the extension:

    $ psql -U <SUPERUSER> -h localhost -d uk_food_hygiene
    uk_food_hygiene=> CREATE EXTENSION postgis;
    CREATE EXTENSION

And run the database migrations:

    $ lein migrate

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 Carlos Vilhena
