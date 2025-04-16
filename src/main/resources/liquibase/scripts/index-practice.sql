-- liquibase formatted sql

-- changeset SmthInUrEye:1
CREATE INDEX facultyNameAndColor_idx ON faculty (name,color)

-- changeset SmthInUrEye:2
CREATE INDEX studentName_idx ON student (name)

