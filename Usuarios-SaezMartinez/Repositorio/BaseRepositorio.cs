using MongoDB.Driver;
using MongoDB.Bson;

namespace usuarios.Repositories
{
    public abstract class BaseRepositorio<T>
    {
        protected readonly IMongoCollection<T> _coleccion;
        private readonly IMongoDatabase _database;

        protected BaseRepositorio(string nombreColeccion)
        {
            var connectionString = Environment.GetEnvironmentVariable("MONGO_CONNECTION_STRING") ?? "mongodb://root:example@localhost:27017/usuariosVerificacion?authSource=admin";

            var cliente = new MongoClient(connectionString);
            _database = cliente.GetDatabase("usuariosVerificacion");

            if (!ColeccionExiste(nombreColeccion))
            {
                _database.CreateCollection(nombreColeccion);
            }

            _coleccion = _database.GetCollection<T>(nombreColeccion);
        }

        private bool ColeccionExiste(string nombreColeccion)
        {
            var filter = new BsonDocument("name", nombreColeccion);
            var colecciones = _database.ListCollections(new ListCollectionsOptions { Filter = filter });
            return colecciones.Any();
        }
    }
}
