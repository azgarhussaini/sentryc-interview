package com.mycompany.myapp.service.dataFetcher;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class GraphQLService {

    @Value("classpath:graphql/sentry.graphqls")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    private MarketPlaceDataFetcher marketPlaceDataFetcher;

    @Autowired
    private ProducerDataFetcher producerDataFetcher;

    @Autowired
    private SellerInfoDataFetcher sellerInfoDataFetcher;

    @Autowired
    private SellersDataFetcher sellersDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        File schemaFile = resource.getFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
            .type(
                "Query",
                typeWiring ->
                    typeWiring
                        .dataFetcher("marketplaces", marketPlaceDataFetcher)
                        .dataFetcher("producers", producerDataFetcher)
                        .dataFetcher("sellerInfos", sellerInfoDataFetcher)
                        .dataFetcher("sellers", sellersDataFetcher)
            )
            .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
