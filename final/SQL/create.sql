-- MySQL Script generated by MySQL Workbench
-- Thu Dec  3 20:33:22 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema expexchangeservice
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `expexchangeservice` ;

-- -----------------------------------------------------
-- Schema expexchangeservice
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `expexchangeservice` DEFAULT CHARACTER SET utf8mb4 ;
USE `expexchangeservice` ;

-- -----------------------------------------------------
-- Table `expexchangeservice`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `role` ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Theme`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Profile` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `full_name` VARCHAR(50) NULL,
  `workplace` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `idUser_idx` (`id_user` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `idUser`
    FOREIGN KEY (`id_user`)
    REFERENCES `expexchangeservice`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Lesson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Lesson` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_theme` INT NOT NULL,
  `id_professor` INT NOT NULL,
  `type` ENUM('SINGLE', 'GROUP') NOT NULL,
  `date` DATE NULL,
  `reward` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `idTheme_idx` (`id_theme` ASC) VISIBLE,
  INDEX `idProfessor_idx` (`id_professor` ASC) VISIBLE,
  CONSTRAINT `idTheme`
    FOREIGN KEY (`id_theme`)
    REFERENCES `expexchangeservice`.`Theme` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLessonProfessor`
    FOREIGN KEY (`id_professor`)
    REFERENCES `expexchangeservice`.`Profile` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Section`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Section` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_section` INT NOT NULL,
  `id_professor` INT NOT NULL,
  `type` ENUM('SINGLE', 'GROUP') NOT NULL,
  `date_start` DATE NULL,
  `count_lesson` INT NULL,
  `reward` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `idSection_idx` (`id_section` ASC) VISIBLE,
  INDEX `idProfessor_idx` (`id_professor` ASC) VISIBLE,
  CONSTRAINT `idSection`
    FOREIGN KEY (`id_section`)
    REFERENCES `expexchangeservice`.`Section` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idCourseProfessor`
    FOREIGN KEY (`id_professor`)
    REFERENCES `expexchangeservice`.`Profile` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Review` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `review` VARCHAR(200) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Lesson_Review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Lesson_Review` (
  `id_lesson` INT NOT NULL,
  `id_review` INT NOT NULL,
  INDEX `idReview_idx` (`id_review` ASC) VISIBLE,
  CONSTRAINT `lessonId`
    FOREIGN KEY (`id_lesson`)
    REFERENCES `expexchangeservice`.`Lesson` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLessonReview`
    FOREIGN KEY (`id_review`)
    REFERENCES `expexchangeservice`.`Review` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Course_Review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Course_Review` (
  `id_course` INT NOT NULL,
  `id_review` INT NOT NULL,
  INDEX `idCourse_idx` (`id_course` ASC) VISIBLE,
  INDEX `idReview_idx` (`id_review` ASC) VISIBLE,
  CONSTRAINT `courseId`
    FOREIGN KEY (`id_course`)
    REFERENCES `expexchangeservice`.`Course` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idCourseReview`
    FOREIGN KEY (`id_review`)
    REFERENCES `expexchangeservice`.`Review` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Section_Themes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Section_Themes` (
  `id_section` INT NOT NULL,
  `id_theme` INT NOT NULL,
  INDEX `idSection_idx` (`id_section` ASC) VISIBLE,
  INDEX `idTheme_idx` (`id_theme` ASC) VISIBLE,
  CONSTRAINT `sectionId`
    FOREIGN KEY (`id_section`)
    REFERENCES `expexchangeservice`.`Section` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `themeId`
    FOREIGN KEY (`id_theme`)
    REFERENCES `expexchangeservice`.`Theme` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Lesson_Members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Lesson_Members` (
  `id_lesson` INT NOT NULL,
  `id_member` INT NOT NULL,
  INDEX `idLesson_idx` (`id_lesson` ASC) VISIBLE,
  INDEX `idMember_idx` (`id_member` ASC) VISIBLE,
  CONSTRAINT `idLesson`
    FOREIGN KEY (`id_lesson`)
    REFERENCES `expexchangeservice`.`Lesson` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLessonMember`
    FOREIGN KEY (`id_member`)
    REFERENCES `expexchangeservice`.`Profile` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Course_Members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Course_Members` (
  `id_course` INT NOT NULL,
  `id_member` INT NOT NULL,
  INDEX `idCourse_idx` (`id_course` ASC) VISIBLE,
  INDEX `idMember_idx` (`id_member` ASC) VISIBLE,
  CONSTRAINT `idCourse`
    FOREIGN KEY (`id_course`)
    REFERENCES `expexchangeservice`.`Course` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idCourseMember`
    FOREIGN KEY (`id_member`)
    REFERENCES `expexchangeservice`.`Profile` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expexchangeservice`.`Token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expexchangeservice`.`Token` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(150) NOT NULL,
  `id_user` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_idx` (`id_user` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `user_id`
    FOREIGN KEY (`id_user`)
    REFERENCES `expexchangeservice`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
