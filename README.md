Fire Inventory
=================================

Project to help track the information needed to run a firehouse.


- Installation

-- Activator installation

Install the Activator Play framework.

Set the environment variable in windows
export SBT_OPTS="-Dsbt.jse.engineType=Node"

Export the environment variable if using a unix based installation
export SBT_OPTS="$SBT_OPTS -Dsbt.jse.engineType=Node"

Clone the repository and then you can use activator in your desighed preference.  
Some use Activator UI or you can just use activator run.

-- MongoDB Installation

Install MongoDB
In the mongo db command prompted exectute the following commands
> use fireHouse
> db.createCollection("people")