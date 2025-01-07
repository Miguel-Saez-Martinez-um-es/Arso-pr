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
            var cliente = new MongoClient("mongodb://localhost:27017");
            _database = cliente.GetDatabase("usuarios");

            // Crear la colección si no existe
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
