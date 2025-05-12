package ma;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Créer une session Spark
        SparkSession spark = SparkSession.builder()
                .appName("Incident Analyzer")
                .master("local[*]")
                .getOrCreate();

        // Charger le fichier CSV
        Dataset<Row> df = spark.read()
                .option("header", true)
                .option("inferSchema", true)
                .csv("src/main/resources/incidents_random.csv");

        // Afficher le schéma pour vérifier
        df.printSchema();

        // 1. Nombre d’incidents par service
        System.out.println("🔸 Nombre d’incidents par service :");
        df.groupBy("service")
                .count()
                .orderBy(functions.desc("count"))
                .show();

        // 2. Extraire l’année de la colonne "date" et compter par année
        Dataset<Row> dfWithYear = df.withColumn("year", functions.year(df.col("date")));

        System.out.println("🔸 Deux années avec le plus d’incidents :");
        dfWithYear.groupBy("year")
                .count()
                .orderBy(functions.desc("count"))
                .limit(2)
                .show();

        spark.stop();
    }
}