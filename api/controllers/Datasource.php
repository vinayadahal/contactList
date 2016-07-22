<?php

class Datasource {

    public function connection() {
        try {
            $dbh = new PDO('mysql:host=localhost;dbname=android_api', 'root', '');
            return $dbh;
        } catch (PDOException $ex) {
            echo $ex;
            die();
        }
        return 0;
    }

}
