-- MySQL Workbench Forward Engineering (Adattato per MindHarbor)

-- Impostazioni iniziali per la sessione SQL
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mindharbor
-- -----------------------------------------------------
-- Database per il progetto ISPW MindHarbor

-- Rimuove lo schema esistente se presente (utile per rieseguire lo script)
DROP SCHEMA IF EXISTS `mindharbor` ;

-- -----------------------------------------------------
-- Schema mindharbor
--
-- Database per il progetto ISPW MindHarbor
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mindharbor` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `mindharbor` ;

-- -----------------------------------------------------
-- Table `mindharbor`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mindharbor`.`Users` (
  `Username` VARCHAR(50) NOT NULL,
  `Password` VARCHAR(60) NOT NULL COMMENT 'BCrypt hashed password',
  `Firstname` VARCHAR(100) NOT NULL,
  `Lastname` VARCHAR(100) NOT NULL,
  `Type` ENUM('PATIENT', 'PSYCHOLOGIST') NOT NULL,
  `Gender` VARCHAR(20) NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB
COMMENT = 'Contiene le informazioni comuni a tutti gli utenti';


-- -----------------------------------------------------
-- Table `mindharbor`.`Psychologists`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mindharbor`.`Psychologists` (
  `Username` VARCHAR(50) NOT NULL,
  `Office` VARCHAR(255) NULL,
  `HourlyCost` DECIMAL(10,2) NULL,
  PRIMARY KEY (`Username`),
  CONSTRAINT `fk_Psychologists_Users`
    FOREIGN KEY (`Username`)
    REFERENCES `mindharbor`.`Users` (`Username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Contiene informazioni specifiche degli psicologi';


-- -----------------------------------------------------
-- Table `mindharbor`.`Patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mindharbor`.`Patients` (
  `Username` VARCHAR(50) NOT NULL,
  `BirthDate` DATE NULL,
  `Psychologist` VARCHAR(50) NULL,
  PRIMARY KEY (`Username`),
  INDEX `fk_Patients_Psychologists_idx` (`Psychologist` ASC) VISIBLE,
  CONSTRAINT `fk_Patients_Users`
    FOREIGN KEY (`Username`)
    REFERENCES `mindharbor`.`Users` (`Username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Patients_Psychologists`
    FOREIGN KEY (`Psychologist`)
    REFERENCES `mindharbor`.`Psychologists` (`Username`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Contiene informazioni specifiche dei pazienti';


-- -----------------------------------------------------
-- Table `mindharbor`.`Appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mindharbor`.`Appointments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `time` TIME NOT NULL,
  `description` TEXT NULL,
  `notified` TINYINT(1) NULL DEFAULT 0 COMMENT '0 = false, 1 = true',
  `patient_username` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Appointments_Patients_idx` (`patient_username` ASC) VISIBLE,
  UNIQUE INDEX `unique_patient_datetime_idx` (`patient_username` ASC, `date` ASC, `time` ASC) VISIBLE,
  CONSTRAINT `fk_Appointments_Patients`
    FOREIGN KEY (`patient_username`)
    REFERENCES `mindharbor`.`Patients` (`Username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Contiene le informazioni sugli appuntamenti';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
