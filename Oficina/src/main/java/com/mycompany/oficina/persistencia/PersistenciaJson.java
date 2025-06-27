package com.mycompany.oficina.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenciaJson {

    private static final String DATA_DIRECTORY = "data";
    private final Gson gson;
    private final Map<String, String> arquivosDeEntidade = new HashMap<>();

    public PersistenciaJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter());

        builder.setPrettyPrinting();
        this.gson = builder.create();

        File dir = new File(DATA_DIRECTORY);
        // --- Linha de Diagnóstico 1 ---
        System.out.println("[DIAGNÓSTICO] O sistema está procurando o diretório de dados aqui: " + dir.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        registrarEntidade("clientes", "clientes.json");
        registrarEntidade("funcionarios", "funcionarios.json");
        registrarEntidade("carros", "carros.json");
        registrarEntidade("pontos", "registros_ponto.json");
        registrarEntidade("ordens_servico", "ordens_servico.json");
        registrarEntidade("agenda", "agenda.json");
        registrarEntidade("estoque", "estoque.json");
        registrarEntidade("financeiro", "financeiro.json");

    }

    private void registrarEntidade(String chave, String nomeArquivo) {
        arquivosDeEntidade.put(chave, DATA_DIRECTORY + "/" + nomeArquivo);
    }

    // --- MÉTODOS PARA LISTAS COM DIAGNÓSTICO ---

    public <T> List<T> carregarLista(String chave, TypeToken<ArrayList<T>> tipoToken) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return new ArrayList<>();

        // --- Linha de Diagnóstico 2 ---
        System.out.println("[DIAGNÓSTICO] Tentando carregar a lista da chave '" + chave + "' do arquivo: " + nomeArquivo);

        List<T> lista = carregar(nomeArquivo, tipoToken.getType());

        // --- Linha de Diagnóstico 3 ---
        if (lista == null) {
            // Isso acontece se o arquivo não existe ou há um erro de sintaxe
            return new ArrayList<>();
        } else {
            System.out.println("[DIAGNÓSTICO] SUCESSO! Para a chave '" + chave + "', foram carregados " + lista.size() + " item(ns).");
            return lista;
        }
    }

    // --- MÉTODOS PRIVADOS COM DIAGNÓSTICO ---

    private <T> T carregar(String nomeArquivo, Type tipo) {
        File file = new File(nomeArquivo);
        if (!file.exists()) {
            // --- Linha de Diagnóstico 4 ---
            System.out.println("[DIAGNÓSTICO] ALERTA: O arquivo '" + nomeArquivo + "' não foi encontrado.");
            return null;
        }

        try (FileReader reader = new FileReader(nomeArquivo)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("[DIAGNÓSTICO] ERRO DE LEITURA no arquivo " + nomeArquivo + ": " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            // --- Linha de Diagnóstico 5 ---
            System.err.println("[DIAGNÓSTICO] ERRO DE SINTAXE no arquivo " + nomeArquivo + ". Verifique se há vírgulas a mais ou chaves/colchetes incorretos. Causa: " + e.getMessage());
            return null;
        }
    }

    // --- OUTROS MÉTODOS (sem alterações) ---

    private String getNomeArquivo(String chave) {
        String nomeArquivo = arquivosDeEntidade.get(chave);
        if (nomeArquivo == null) {
            System.err.println("ERRO: Nenhuma entidade registrada para a chave: " + chave);
            return null;
        }
        return nomeArquivo;
    }

    public <T> void salvarLista(String chave, List<T> lista) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return;
        salvar(lista, nomeArquivo);
    }

    private <T> void salvar(T dados, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO ao salvar o arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    public <K, V> void salvarMapa(String chave, Map<K, V> mapa) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return;
        salvar(mapa, nomeArquivo);
    }

    public <K, V> Map<K, V> carregarMapa(String chave, TypeToken<HashMap<K, V>> tipoToken) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return new HashMap<>();

        Map<K, V> mapa = carregar(nomeArquivo, tipoToken.getType());
        return mapa != null ? mapa : new HashMap<>();
    }
}