-- support hstore as type
CREATE DOMAIN hstore AS OTHER;
CREATE ALIAS IF NOT EXISTS toKeyValue FOR "net.canadensys.databaseutils.H2Decode.toKeyValue"
