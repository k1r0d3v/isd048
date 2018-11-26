-- -----------------------------------------------------------------------------
-- Model
--------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE ReservationTable;
DROP TABLE ShowTable;

-- --------------------------------- Show -------------------------------------
CREATE TABLE ShowTable (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    startDate DATETIME NOT NULL,
    duration BIGINT NOT NULL,
    limitDate DATETIME NOT NULL,
    maxTickets BIGINT NOT NULL,
    availableTickets BIGINT NOT NULL,
    realPrice FLOAT NOT NULL,
    discountedPrice FLOAT NOT NULL,
    salesCommission FLOAT NOT NULL,

    CONSTRAINT ShowPK PRIMARY KEY(id) ) ENGINE = InnoDB;

-- --------------------------------- Reservation ------------------------------
CREATE TABLE ReservationTable (
    id BIGINT NOT NULL AUTO_INCREMENT,
    showId BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    cardNumber VARCHAR(255) NOT NULL,
    tickets BIGINT NOT NULL,
    isValid BOOL NOT NULL,
    code VARCHAR(255) NOT NULL,
    reservationDate DATETIME NOT NULL,
    price FLOAT NOT NULL,

    CONSTRAINT ReservationPK PRIMARY KEY(id),

    CONSTRAINT ReservationShowIdFK FOREIGN KEY(showId)
        REFERENCES ShowTable(id) ON DELETE CASCADE ) ENGINE = InnoDB;