package vn.com.vtcc.apiExpose.utils.transform;

class QueryParsingTest {
    public static void main(String[] args) {
        String a = QueryParsing.parse("{\n" +
                "    \"article_type\": \"userprofile\",\n" +
                "    \"fields\": [\n" +
                "      \"all\"\n" +
                "    ],\n" +
                "    \"filter\": {\n" +
                "      \"name\": {\n" +
                "        \"like\": \"linhnv52\"\n" +
                "      },\n" +
                "      \"age\": {\n" +
                "        \"lt\": \"50\",\n" +
                "      },\n" +
                "      \"birthday\": {\n" +
                "        \"start\": \"2020-09-29 21:10:50\"\n" +
                "      },\n" +
                "      \"single\": {\n" +
                "        \"is\": \"true\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"fields_exclude\": [\n" +
                "    ]\n" +
                "  }","config/metadata");
        System.out.println(a);
    }
}