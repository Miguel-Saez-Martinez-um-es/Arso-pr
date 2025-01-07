using System;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace usuarios.Models
{
    public class CodigoActivacion
    {
        // Identificador único del código de activación (para MongoDB)
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        // Identificador del usuario asociado
        public string UsuarioId { get; set; }

        // Código generado
        public string Codigo { get; set; }

        // Fecha de expiración del código
        public DateTime FechaExpiracion { get; set; }

        // Constructor
        public CodigoActivacion(string usuarioId, TimeSpan tiempoValidez)
        {
            UsuarioId = usuarioId;
            Codigo = GenerarCodigo();
            FechaExpiracion = DateTime.UtcNow.Add(tiempoValidez);
        }

        // Método para generar un código aleatorio
        private string GenerarCodigo()
        {
            return Guid.NewGuid().ToString().Replace("-", "").Substring(0, 8).ToUpper();
        }

        // Método para verificar si el código es válido
        public bool EsValido()
        {
            return DateTime.UtcNow <= FechaExpiracion;
        }
    }
}
