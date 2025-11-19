package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorDatos {
    private Random random;

    public SimuladorDatos() {
        this.random = new Random();
    }

    public List<Metrica> simularPeriodo(String usuarioId, String dispositivoId,
                                        int meses, int metricasPorDia, String tipoDispositivo) {
        List<Metrica> metricas = new ArrayList<>();
        LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(meses);
        LocalDateTime fechaActual = fechaInicio;
        LocalDateTime fechaFin = LocalDateTime.now();

        while (fechaActual.isBefore(fechaFin)) {
            for (int i = 0; i < metricasPorDia; i++) {
                LocalDateTime timestamp = fechaActual.plusHours(i * (24 / metricasPorDia));

                switch (tipoDispositivo) {
                    case "SmartWatch":
                        metricas.addAll(generarMetricasSmartWatch(usuarioId, dispositivoId, timestamp));
                        break;
                    case "Pulsera":
                        metricas.addAll(generarMetricasPulsera(usuarioId, dispositivoId, timestamp));
                        break;
                    case "Oximetro":
                        metricas.addAll(generarMetricasOximetro(usuarioId, dispositivoId, timestamp));
                        break;
                }
            }
            fechaActual = fechaActual.plusDays(1);
        }
        return metricas;
    }

    private List<Metrica> generarMetricasSmartWatch(String usuarioId, String dispositivoId, LocalDateTime timestamp) {
        List<Metrica> metricas = new ArrayList<>();

        int fc = generarFCSegunHora(timestamp.getHour());
        double var = 30 + random.nextDouble() * 50;
        MetricaCardiaca mc = new MetricaCardiaca(dispositivoId, usuarioId, fc, var);
        mc.setTimestamp(timestamp);
        metricas.add(mc);

        if (timestamp.getHour() >= 6 && timestamp.getHour() <= 22) {
            int pasos = generarPasosSegunHora(timestamp.getHour());
            double dist = pasos * 0.0008;
            double cal = pasos * 0.04;
            MetricaActividad ma = new MetricaActividad(dispositivoId, usuarioId, pasos, dist, cal);
            ma.setTimestamp(timestamp);
            metricas.add(ma);
        }

        if (timestamp.getHour() >= 22 || timestamp.getHour() <= 6) {
            double horas = 6 + random.nextDouble() * 3;
            int calidad = 60 + random.nextInt(40);
            int rem = 2 + random.nextInt(3);
            MetricaSueno ms = new MetricaSueno(dispositivoId, usuarioId, horas, calidad, rem);
            ms.setTimestamp(timestamp);
            metricas.add(ms);
        }

        return metricas;
    }

    private List<Metrica> generarMetricasPulsera(String usuarioId, String dispositivoId, LocalDateTime timestamp) {
        List<Metrica> metricas = new ArrayList<>();

        if (timestamp.getHour() >= 6 && timestamp.getHour() <= 22) {
            int pasos = generarPasosSegunHora(timestamp.getHour());
            double dist = pasos * 0.0008;
            double cal = pasos * 0.04;
            MetricaActividad ma = new MetricaActividad(dispositivoId, usuarioId, pasos, dist, cal);
            ma.setTimestamp(timestamp);
            metricas.add(ma);
        }

        int fc = 65 + random.nextInt(35);
        MetricaCardiaca mc = new MetricaCardiaca(dispositivoId, usuarioId, fc, 40);
        mc.setTimestamp(timestamp);
        metricas.add(mc);

        return metricas;
    }

    private List<Metrica> generarMetricasOximetro(String usuarioId, String dispositivoId, LocalDateTime timestamp) {
        List<Metrica> metricas = new ArrayList<>();

        double spo2 = 95 + random.nextDouble() * 5;
        MetricaOxigeno mo = new MetricaOxigeno(dispositivoId, usuarioId, spo2);
        mo.setTimestamp(timestamp);
        metricas.add(mo);

        int fc = generarFCSegunHora(timestamp.getHour());
        MetricaCardiaca mc = new MetricaCardiaca(dispositivoId, usuarioId, fc, 45);
        mc.setTimestamp(timestamp);
        metricas.add(mc);

        return metricas;
    }

    private int generarFCSegunHora(int hora) {
        if (hora >= 22 || hora <= 6) {
            return 55 + random.nextInt(15);
        } else if (hora >= 12 && hora <= 14) {
            return 70 + random.nextInt(20);
        } else {
            return 60 + random.nextInt(20);
        }
    }

    private int generarPasosSegunHora(int hora) {
        if (hora >= 7 && hora <= 9) {
            return 300 + random.nextInt(500);
        } else if (hora >= 12 && hora <= 14) {
            return 500 + random.nextInt(500);
        } else if (hora >= 18 && hora <= 20) {
            return 400 + random.nextInt(500);
        } else {
            return 100 + random.nextInt(300);
        }
    }

    public List<Metrica> simularMultiplesDispositivos(UsuarioPrincipal usuario, int meses) {
        List<Metrica> todasMetricas = new ArrayList<>();

        List<DispositivoWearable> dispositivos = usuario.getGestorDispositivos().getDispositivos();
        for (int i = 0; i < dispositivos.size(); i++) {
            DispositivoWearable dispositivo = dispositivos.get(i);
            String tipo = dispositivo.getTipoDispositivo();
            int metricasPorDia = 4;

            String tipoSimulacion = "SmartWatch"; // por defecto
            if (tipo.contains("Pulsera")) {
                tipoSimulacion = "Pulsera";
            } else if (tipo.contains("Oxímetro")) {
                tipoSimulacion = "Oximetro";
            }

            List<Metrica> metricas = simularPeriodo(
                    usuario.getIdUsuario(),
                    dispositivo.getIdDispositivo(),
                    meses,
                    metricasPorDia,
                    tipoSimulacion
            );

            // Agregar todas las métricas de este dispositivo
            for (int j = 0; j < metricas.size(); j++) {
                todasMetricas.add(metricas.get(j));
            }
        }

        return todasMetricas;
    }
}
