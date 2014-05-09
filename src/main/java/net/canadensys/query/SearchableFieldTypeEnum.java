package net.canadensys.query;

/**
 * The SearchableFieldTypeEnum represents the composition of the SearchableField.
 * A SearchableField can aggregates 2 (or more) real database fields to ease the search.
 * This is also used to determine the right Interpreter.
 * @author canandesys
 *
 */
public enum SearchableFieldTypeEnum {
	SINGLE_VALUE,
	MIN_MAX_NUMBER,
	START_END_DATE,
	INSIDE_ENVELOPE_GEO,
	INSIDE_POLYGON_GEO,
	WITHIN_RADIUS_GEO,
	GEO_PREDEFINED_AREA;
}
