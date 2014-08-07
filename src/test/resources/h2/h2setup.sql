-- support hstore as type
CREATE DOMAIN IF NOT EXISTS hstore AS OTHER;
CREATE ALIAS IF NOT EXISTS toKeyValue FOR "net.canadensys.databaseutils.H2Decode.toKeyValue"
