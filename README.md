# uk-food-hygiene

Show the food hygiene ratings attributed to all London restaurants, by borough.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

First, make sure you have [PostgreSQL](http://www.postgresql.org/) installed.
With `postgresql` running, create the database:

    $ createdb uk_food_hygiene

And run the database migrations:

    $ lein migrate

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 Carlos Vilhena
